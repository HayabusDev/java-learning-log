package tikectSalesSimulator.service;

import tikectSalesSimulator.domain.Event;
import tikectSalesSimulator.domain.OrderStatus;
import tikectSalesSimulator.domain.SaleStatus;
import tikectSalesSimulator.domain.TicketOrder;
import tikectSalesSimulator.repository.EventRepository;
import tikectSalesSimulator.repository.OrderRepository;
import tikectSalesSimulator.result.Result;
import tikectSalesSimulator.result.SafeAction;
import tikectSalesSimulator.result.SystemErrorCode;
import tikectSalesSimulator.validator.TicketValidationRule;
import tikectSalesSimulator.validator.TicketValidator;

import java.util.*;


public class TicketService {

    // createEvent()
    // deleteEvent()
    // listEvent() sortとsearchは今回ナシ？あとで拡張でも？

    // purchaseTicket() → createOrder, remainSeats減る
    // cancelTiket() →  orderをcancelにする, remainSeats増える
    // changeSaleStatus() → DRAFTからON_SALE と 販売期間終了想定でON_SALE or SOLD_OUT からCLOSED

    // listOrderは後で注文履歴を実装するなら

    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final TicketValidator ticketValidator;

    public TicketService(EventRepository eventRepository, OrderRepository orderRepository, TicketValidator ticketValidator){
        this.eventRepository = eventRepository;
        this.orderRepository = orderRepository;
        this.ticketValidator = ticketValidator;
    }

    public Result<Void> createEvent(String eventName, int allSeats){
        TicketValidationRule createRules = ticketValidator.validateCreateEvent(eventName, allSeats);

        //入力チェック
        if (createRules != null){
            return Result.failure(createRules.getCode());
        }

        //重複チェック(eventNameのみ)
        Result<Void> isExist = checkDuplicateEventName(eventName);
        if (!isExist.isSuccess()){
            return isExist;
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                //作成
                UUID eventId = UUID.randomUUID();
                Event createdEvent = new Event(eventName, eventId, SaleStatus.DRAFT, allSeats, allSeats); //作成時はallSeats = remainSeats
                eventRepository.saveEvent(createdEvent);
                return Result.success();
            }
        };

        return executeSafely(action, "createEvent", eventName);
    }

    private Result<Void> checkDuplicateEventName(String eventName){
        Map<UUID, Event> existingEvents = eventRepository.findAllEvents();
        for (Event event : existingEvents.values()){
            if (Objects.equals(event.getEventName(), eventName)){
                return Result.failure(SystemErrorCode.DATA_DUPLICATE);
            }
        }
        return Result.success();
    }

    public Result<Void> deleteEvent(UUID eventId){
        //UI側で、Event一覧→番号選択で削除
        TicketValidationRule deleteRules = ticketValidator.validateDeleteEvent(eventId);

        //入力チェック
        if (deleteRules != null){
            return Result.failure(deleteRules.getCode());
        }

        Event event = eventRepository.findEventById(eventId);
        //存在チェック
        if (event == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        //削除条件(saleStatus = CLOSED のみ)
        if (event.getSaleStatus() != SaleStatus.CLOSED){
            return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                //削除
                eventRepository.deleteEventById(eventId);
                return Result.success();
            }
        };

        return executeSafely(action, "deleteEvent", eventId.toString());
    }

    public Result<List<Event>> listEvents(){
        SafeAction<List<Event>> action = new SafeAction<List<Event>>() {
            @Override
            public Result<List<Event>> execute() {
                //空Listも変える、空は見つかりません表示
                return Result.success(convertMapToList());
            }
        };

        return executeSafely(action, "listEvents", "no-input");
    }

    //MapをListに変換
    private List<Event> convertMapToList(){
        return new ArrayList<>(eventRepository.findAllEvents().values());
    }

    public Result<Void> purchaseTicket(String buyerName, UUID eventId, int purchaseQuantity){
        //UI側で、Event一覧→番号選択で購入
        TicketValidationRule purchaseRules = ticketValidator.validatePurchase(eventId, buyerName, purchaseQuantity);

        //入力チェック
        if (purchaseRules != null){
            return Result.failure(purchaseRules.getCode());
        }

        Event event = eventRepository.findEventById(eventId);
        //存在チェック
        if (event == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                //残り座席数減らす
                boolean canPurchase = event.purchase(purchaseQuantity);
                if (!canPurchase){
                    return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
                }
                //購入処理
                try {
                    //Order 生成
                    UUID orderId = UUID.randomUUID();
                    TicketOrder createdOrder = new TicketOrder(buyerName, orderId, eventId, purchaseQuantity, OrderStatus.ACTIVE); //OrderStatusはACTIVEで生成
                    //保存
                    orderRepository.saveOrder(createdOrder);
                }catch (RuntimeException e){
                    //失敗したら在庫を戻す（補償トランザクション）
                    event.cancel(purchaseQuantity);
                    throw e;
                }
                return Result.success();
            }
        };

        return executeSafely(action, "purchaseTicket", eventId.toString());
    }

    public Result<Void> cancelTicket(UUID orderId){
        TicketValidationRule cancelRules = ticketValidator.validateCancel(orderId);

        //入力チェック
        if (cancelRules != null){
            return Result.failure(cancelRules.getCode());
        }

        //存在チェック
        TicketOrder order = orderRepository.findOrderById(orderId);
        if (order == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        //order 条件チェック
        if (!order.canCancel()){
            return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
        }

        Event event = eventRepository.findEventById(order.getEventId());
        //存在チェック
        if (event == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        int purchasedQuantity = order.getPurchaseQuantity();

        //event 条件チェック
        if (!event.cancelable(purchasedQuantity)){
            return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                //キャンセル処理(orderStatusをACTIVEからCANCELEDへ)
                order.changeToCancelStatus();

                //残り座席数増やす
                event.cancel(purchasedQuantity);
                return Result.success();
            }
        };

        return executeSafely(action, "cancelTicket", orderId.toString());
    }

    public Result<Void> changeSaleStatus(UUID eventId, SaleStatus nextStatus){
        TicketValidationRule changeStatusRules = ticketValidator.validateChangeSaleStatus(eventId, nextStatus);

        //入力チェック
        if (changeStatusRules != null){
            return Result.failure(changeStatusRules.getCode());
        }

        Event event = eventRepository.findEventById(eventId);
        //存在チェック
        if (event == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                //saleStatus 変更処理
                boolean canChangeSaleStatus = event.changeStatus(nextStatus);
                if (!canChangeSaleStatus){
                    return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
                }
                return Result.success();
            }
        };

        return executeSafely(action, "changeSaleStatus", eventId.toString() + " / " +nextStatus);
    }

    //SafeAction
    private <T> Result<T> executeSafely(SafeAction<T> action, String operationName, String inputValue){
        try{
            return action.execute();

        }catch (IllegalArgumentException iae){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            iae.printStackTrace();

            return Result.failure(SystemErrorCode.INPUT_INVALID);

        }catch (IllegalStateException ise){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            ise.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }catch (RuntimeException re){

            System.err.println("[FATAL] " + operationName);
            System.err.println("input=" + inputValue);
            re.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }
    }
}
