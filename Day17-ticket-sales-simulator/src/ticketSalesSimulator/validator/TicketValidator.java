package tikectSalesSimulator.validator;

import tikectSalesSimulator.domain.SaleStatus;

import java.util.UUID;

public class TicketValidator {

    //Eventチェック
    public TicketValidationRule validateCreateEvent(String eventName, int allSeats){

        if (eventName == null || eventName.isBlank()){
            return TicketValidationRule.EVENT_NAME_NOT_EMPTY;
        }
        if (allSeats <= 0){
            return TicketValidationRule.ALL_SEATS_POSITIVE;
        }
        return null;
    }

    public TicketValidationRule validateDeleteEvent(UUID eventId){
        return validateEventId(eventId);
    }

    // 購入関連
    public TicketValidationRule validatePurchase(UUID eventId, String buyerName, int quantity){
        TicketValidationRule eventIdRule = validateEventId(eventId);
        TicketValidationRule buyerNameRule = validateBuyerName(buyerName);
        TicketValidationRule quantityRule = validateQuantity(quantity);

        if (eventIdRule != null){
            return eventIdRule;
        }
        if (buyerNameRule != null){
            return buyerNameRule;
        }
        if (quantityRule != null){
            return quantityRule;
        }

        return null;
    }

    public TicketValidationRule validateCancel(UUID orderId){
        return validateOrderId(orderId);
    }

    public TicketValidationRule validateChangeSaleStatus(UUID eventId, SaleStatus saleStatus){
        TicketValidationRule eventIdRule = validateEventId(eventId);

        if (eventIdRule != null){
            return eventIdRule;
        }

        if (saleStatus == null){
            return TicketValidationRule.SALE_STATUS_NOT_NULL;
        }

        return null;
    }

    //個別チェック
    private TicketValidationRule validateEventId(UUID eventId){
        if (eventId == null){
            return TicketValidationRule.EVENT_ID_NOT_NULL;
        }
        return null;
    }

    private TicketValidationRule validateOrderId(UUID orderId){
        if (orderId == null){
            return TicketValidationRule.TICKET_ORDER_ID_NOT_NULL;
        }
        return null;
    }

    //nullチェック、空チェック
    private TicketValidationRule validateBuyerName(String buyerName){
        if (buyerName == null){
            return TicketValidationRule.BUYER_NAME_NOT_EMPTY;
        }
        if (buyerName.isBlank()){
            return TicketValidationRule.BUYER_NAME_NOT_EMPTY;
        }
        return null;
    }

    private TicketValidationRule validateQuantity(int quantity){
        if (quantity <= 0){
            return TicketValidationRule.QUANTITY_POSITIVE;
        }
        return null;
    }
}
