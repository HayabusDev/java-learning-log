package tikectSalesSimulator.result;

public enum TicketErrorCode implements ErrorCodeLike {
    EVENT_NAME_EMPTY("EVENT_001", "eventNameが空です。"),
    ALL_SEATS_NOT_POSITIVE("EVENT_002", "最大座席数が0以下です。"),
    REMAIN_SEATS_NEGATIVE("EVENT_003", "残り座席数が負数になっています。"),
    REMAIN_SEATS_BIGGER_ALL_SEATS("EVENT_004", "残り座席数が最大座席数を超えています。"),
    SALE_STATUS_NULL("EVENT_005", "saleStatus情報がありません。"),

    QUANTITY_NOT_POSITIVE("PURCHASE_001", "quantityが0以下です。"),
    BUYER_NAME_EMPTY("PURCHASE_002", "buyerNameが空です。"),
    ORDER_STATUS_NULL("PURCHASE_003", "orderStatusがありません。"),


    EVENT_ID_NULL("ID_001", "eventIdがnullです。"),
    TICKET_ORDER_ID_NULL("ID_002", "orderIdがnullです。"),

    UNKNOWN_ERROR("UNKNOWN", "不明なエラーが発生しました。");

    private final String code;
    private final String message;

    TicketErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    //codeからEnumを取得
    public static TicketErrorCode fromCode(String code){
        if (code == null){
            return UNKNOWN_ERROR;
        }

        for (TicketErrorCode error : values()){
            if (error.code.equals(code)){
                return error;
            }
        }

        //見つからなかった場合は不明なエラー
        return UNKNOWN_ERROR;
    }
}
