package tikectSalesSimulator.validator;

import tikectSalesSimulator.result.TicketErrorCode;

public enum TicketValidationRule {
    EVENT_NAME_NOT_EMPTY(TicketErrorCode.EVENT_NAME_EMPTY),
    ALL_SEATS_POSITIVE(TicketErrorCode.ALL_SEATS_NOT_POSITIVE),
    REMAIN_SEATS_NON_NEGATIVE(TicketErrorCode.REMAIN_SEATS_NEGATIVE),
    REMAIN_SEATS_NOT_BIGGER_ALL_SEATS(TicketErrorCode.REMAIN_SEATS_BIGGER_ALL_SEATS),

    QUANTITY_POSITIVE(TicketErrorCode.QUANTITY_NOT_POSITIVE),
    BUYER_NAME_NOT_EMPTY(TicketErrorCode.BUYER_NAME_EMPTY),

    EVENT_ID_NOT_NULL(TicketErrorCode.EVENT_ID_NULL),
    TICKET_ORDER_ID_NOT_NULL(TicketErrorCode.TICKET_ORDER_ID_NULL);

    private final TicketErrorCode code;

    TicketValidationRule(TicketErrorCode code){
        this.code = code;
    }

    public TicketErrorCode getCode() {
        return code;
    }
}
