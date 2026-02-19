package tikectSalesSimulator.controller;

import tikectSalesSimulator.domain.Event;
import tikectSalesSimulator.domain.SaleStatus;
import tikectSalesSimulator.domain.TicketOrder;
import tikectSalesSimulator.result.ErrorCodeLike;
import tikectSalesSimulator.result.Result;
import tikectSalesSimulator.service.TicketService;
import tikectSalesSimulator.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    public void launch(){
        printMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 7);

            switch (userInput) {
                case 1:
                    handleCreateEvent();
                    break;
                case 2:
                    handleListEvents();
                    break;
                case 3:
                    handleChangeSaleStatus();
                    break;
                case 4:
                    handlePurchaseTicket();
                    break;
                case 5:
                    handleCancelTicket();
                    break;
                case 6:
                    handleDeleteEvent();
                    break;
                case 7:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void printMenu() {
        System.out.println("=====================");
        System.out.println("チケット販売システム");
        System.out.println("1. イベント作成");
        System.out.println("2. イベント一覧");
        System.out.println("3. 販売状態変更");
        System.out.println("4. チケット購入");
        System.out.println("5. チケットキャンセル");
        System.out.println("6. イベント削除");
        System.out.println("7. 終了");
        System.out.println("=====================");
    }

    private void handleCreateEvent() {
        String eventName = InputUtil.readNonEmptyString("イベント名を入力してください。");
        int allSeats = InputUtil.readIntInRange("販売する座席数を入力してください。", 1 , Event.MAX_SEATS );

        boolean userYesNo = InputUtil.confirmYesNo("イベント名: " + eventName + " | " + "最大座席数: " + allSeats + " を作成します。" +
                "\nよろしいですか？ (y / n)");

        if (!userYesNo){
            System.out.println("イベント作成を中止しました");
            return;
        }

        Result<Void> createResult = ticketService.createEvent(eventName, allSeats);
        if (!createResult.isSuccess()){
            printResultErrors(createResult);
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println("イベント名: " + eventName + " | " + "最大座席数: " + allSeats + " を作成しました。");
    }

    private void handleListEvents() {
        Result<List<Event>> eventListResult = ticketService.listEvents();

        if (!eventListResult.isSuccess()){
            printResultErrors(eventListResult);
            return;
        }

        List<Event> eventList = eventListResult.getData();

        if (eventList.isEmpty()){
            System.out.println("登録イベントはありません。");
            return;
        }

        System.out.println("全 " + eventList.size() + " 件");
        listEventWithNum(eventList);
    }

    private void handleChangeSaleStatus() {
        Result<List<Event>> eventListResult = ticketService.listEvents();

        if (!eventListResult.isSuccess()){
            printResultErrors(eventListResult);
            return;
        }

        List<Event> eventList = eventListResult.getData();

        if (eventList.isEmpty()){
            System.out.println("登録イベントはありません。");
            return;
        }

        listEventWithNum(eventList);
        int userChoice = InputUtil.readIntInRange("販売状態を変更するイベントを選択してください。", 1, eventList.size());
        Event selectedEvent = eventList.get(userChoice - 1);
        SaleStatus current = selectedEvent.getSaleStatus();

        if (current == SaleStatus.CLOSED){
            System.out.println("販売終了のため変更できません");
            return;
        }

        if (current == SaleStatus.DRAFT){
            boolean userYesNo = InputUtil.confirmYesNo( selectedEvent.getEventName()
                    + " / " + current
                    + " の販売開始しますか？(y / n)");
            if (!userYesNo){
                System.out.println("販売開始への変更を中止しました。");
                return;
            }
            Result<Void> startSaleResult = ticketService.startSale(selectedEvent.getEventId());
            if (!startSaleResult.isSuccess()){
                printResultErrors(startSaleResult);
                System.out.println("最初からやり直してください。");
                return;
            }
            System.out.println(selectedEvent.getEventName() + " の販売開始しました。");
            return;
        }

        if (current == SaleStatus.ON_SALE || current == SaleStatus.SOLD_OUT){
            boolean userYesNo = InputUtil.confirmYesNo( selectedEvent.getEventName()
                    + " / " + current
                    + " の販売を終了しますか？(y / n)");
            if (!userYesNo){
                System.out.println("販売終了への変更を中止しました。");
                return;
            }
            Result<Void> closeSaleResult = ticketService.closeSale(selectedEvent.getEventId());
            if (!closeSaleResult.isSuccess()){
                printResultErrors(closeSaleResult);
                System.out.println("最初からやり直してください。");
                return;
            }
            System.out.println(selectedEvent.getEventName() + " の販売を終了しました。");
        }
    }

    private void handlePurchaseTicket() {
        Result<List<Event>> eventListResult = ticketService.listEvents();

        if (!eventListResult.isSuccess()){
            printResultErrors(eventListResult);
            return;
        }

        List<Event> eventList = eventListResult.getData();

        if (eventList.isEmpty()){
            System.out.println("登録イベントはありません。");
            return;
        }

        List<Event> onSaleEvents = new ArrayList<>();

        for (Event event : eventList) {
            if (event.getSaleStatus() == SaleStatus.ON_SALE) {
                onSaleEvents.add(event);
            }
        }

        if (onSaleEvents.isEmpty()){
            System.out.println("販売中のチケットはありません。");
            return;
        }

        listEventWithNum(onSaleEvents);

        int userChoice = InputUtil.readIntInRange("チケットを購入するイベントを選択してください。", 1, onSaleEvents.size());
        Event selectedEvent = onSaleEvents.get(userChoice - 1);
        String buyerName = InputUtil.readNonEmptyString("購入者名を入力してください。");
        int purchaseQuantity = InputUtil.readIntInRange("購入する枚数を入力してください。最大" +
                TicketOrder.MAX_QUANTITY + "枚まで購入可能です。", 1, TicketOrder.MAX_QUANTITY);

        boolean userYesNo = InputUtil.confirmYesNo(buyerName + " 様: " + selectedEvent.getEventName() + " のチケットを"
                + purchaseQuantity + " 枚購入します。よろしいですか？(y / n)" );

        if (!userYesNo){
            System.out.println("購入をキャンセルしました。");
            return;
        }

        Result<TicketOrder> purchaseResult = ticketService.purchaseTicket(buyerName, selectedEvent.getEventId(), purchaseQuantity);
        if (!purchaseResult.isSuccess()){
            printResultErrors(purchaseResult);
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println(buyerName + " 様: " + selectedEvent.getEventName() + " のチケットを"
                + purchaseQuantity + " 枚購入しました。" + "\nオーダーID: " + purchaseResult.getData().getOrderId());
    }

    private void handleCancelTicket() {
        UUID orderId = InputUtil.readUUID("オーダーIDを入力してください。");

        boolean userYesNo = InputUtil.confirmYesNo("オーダーID: " + orderId + " のチケットをキャンセルします。よろしいですか？(y / n)");
        if (!userYesNo){
            System.out.println("キャンセルを中止しました。");
            return;
        }

        Result<Void> cancelResult = ticketService.cancelTicket(orderId);
        if (!cancelResult.isSuccess()){
            printResultErrors(cancelResult);
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println("オーダーID: " + orderId + " のチケットをキャンセルしました。");
    }

    private void handleDeleteEvent() {
        Result<List<Event>> eventListResult = ticketService.listEvents();

        if (!eventListResult.isSuccess()){
            printResultErrors(eventListResult);
            return;
        }

        List<Event> eventList = eventListResult.getData();

        if (eventList.isEmpty()){
            System.out.println("登録イベントはありません。");
            return;
        }

        List<Event> closedEvents = new ArrayList<>();

        for (Event event : eventList) {
            if (event.getSaleStatus() == SaleStatus.CLOSED) {
                closedEvents.add(event);
            }
        }

        if (closedEvents.isEmpty()){
            System.out.println("終了したイベントはありません。");
            return;
        }

        listEventWithNum(closedEvents);

        int userChoice = InputUtil.readIntInRange("削除するイベントを選択してください。", 1, closedEvents.size());
        Event selectedEvent = closedEvents.get(userChoice - 1);

        boolean userYesNo = InputUtil.confirmYesNo("イベント名:" + selectedEvent.getEventName() + " を削除します。よろしいですか？(y / n)");
        if (!userYesNo){
            System.out.println("削除処理を中止しました。");
            return;
        }

        Result<Void> deleteResult = ticketService.deleteEvent(selectedEvent.getEventId());
        if(!deleteResult.isSuccess()){
            printResultErrors(deleteResult);
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println("削除しました。");
    }

    private void listEventWithNum(List<Event> eventList){
        for (int i = 0; i < eventList.size(); i++){
            System.out.println(i+1 + ": " + eventList.get(i));
        }
    }

    private void printResultErrors(Result<?> can){
        for (ErrorCodeLike errorCode : can.getErrorCodeLikes()){
            System.out.println(errorCode.getMessage());
        }
    }
}
