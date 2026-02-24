package inventoryManagementSystem.validator;

import inventoryManagementSystem.result.ItemErrorCode;

public enum ItemValidationRule {
    ITEM_NAME_NOT_EMPTY(ItemErrorCode.ITEM_NAME_EMPTY),
    ITEM_ID_NOT_NULL(ItemErrorCode.ITEM_ID_NULL),

    AMOUNT_NOT_NULL(ItemErrorCode.AMOUNT_NULL),
    AMOUNT_POSITIVE(ItemErrorCode.AMOUNT_NOT_POSITIVE),
    REORDER_POINT_NOT_NULL(ItemErrorCode.REORDER_POINT_NULL),
    LOW_STOCK_THRESHOLD_NOT_NULL(ItemErrorCode.LOW_STOCK_THRESHOLD_NULL);

    private final ItemErrorCode code;

    ItemValidationRule(ItemErrorCode code) {
        this.code = code;
    }

    public ItemErrorCode getCode() {
        return code;
    }
}
