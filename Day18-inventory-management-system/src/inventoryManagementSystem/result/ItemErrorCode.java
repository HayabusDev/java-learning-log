package inventoryManagementSystem.result;

public enum ItemErrorCode implements ErrorCodeLike {
    ITEM_NAME_EMPTY("ITEM_001", "itemNameが空です。"),
    ITEM_ID_NULL("ITEM_002", "itemIdがnullです。"),
    ITEM_DATA_NOT_FOUND("ITEM_003", "商品が登録されていません。"),
    ITEM_NAME_DUPLICATE("ITEM_004", "既に同名の商品が登録されています。"),

    AMOUNT_NULL("QTY_001", "amountがnullです。" ),
    AMOUNT_NOT_POSITIVE("QTY_002", "amountが0以下です。"),
    REORDER_POINT_NULL("QTY_003", "reorderPointがnullです。"),
    LOW_STOCK_THRESHOLD_NULL("QTY_004", "lowStockThresholdがnullです。"),
    INSUFFICIENT_STOCK("QTY_005", "出荷数が在庫数を超えています。"),

    UNKNOWN_ERROR("UNKNOWN", "不明なエラーが発生しました。");


    private final String code;
    private final String message;

    ItemErrorCode(String code, String message) {
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
    public static ItemErrorCode fromCode(String code){
        if (code == null){
            return UNKNOWN_ERROR;
        }

        for (ItemErrorCode error : values()){
            if (error.code.equals(code)){
                return error;
            }
        }

        //見つからなかった場合は不明なエラー
        return UNKNOWN_ERROR;
    }
}
