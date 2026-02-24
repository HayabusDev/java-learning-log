package inventoryManagementSystem.validator;

import inventoryManagementSystem.result.ErrorCodeLike;
import inventoryManagementSystem.result.Result;
import inventoryManagementSystem.valueObject.ItemId;
import inventoryManagementSystem.valueObject.LowStockThreshold;
import inventoryManagementSystem.valueObject.Quantity;
import inventoryManagementSystem.valueObject.ReorderPoint;

import java.util.ArrayList;
import java.util.List;

public class ItemValidator {

    public Result<Void> validateRegister(String itemName, Quantity initialQty, ReorderPoint reorderPoint, LowStockThreshold lowStockThreshold){
        List<ErrorCodeLike> errors = new ArrayList<>();

        if (itemName == null || itemName.isBlank()){
            errors.add(ItemValidationRule.ITEM_NAME_NOT_EMPTY.getCode());
        }

        if (initialQty == null){
            errors.add(ItemValidationRule.AMOUNT_NOT_NULL.getCode());
        }

        if (reorderPoint == null){
            errors.add(ItemValidationRule.REORDER_POINT_NOT_NULL.getCode());
        }

        if (lowStockThreshold == null){
            errors.add(ItemValidationRule.LOW_STOCK_THRESHOLD_NOT_NULL.getCode());
        }

        if (!errors.isEmpty()){
            return Result.failure(errors);
        }

        return Result.success();
    }

    public Result<Void> validateReceiveAndShip(ItemId itemId, Quantity amount){
        List<ErrorCodeLike> errors = new ArrayList<>();

        if (itemId == null){
            errors.add(ItemValidationRule.ITEM_ID_NOT_NULL.getCode());
        }

        if (amount == null){
            errors.add(ItemValidationRule.AMOUNT_NOT_NULL.getCode());
        }else if (amount.getQuantity() <= 0){
            errors.add(ItemValidationRule.AMOUNT_POSITIVE.getCode());
        }

        if (!errors.isEmpty()){
           return Result.failure(errors);
        }

        return Result.success();
    }

    public Result<Void> validateItemId(ItemId itemId){
        if (itemId == null){
            return Result.failure(ItemValidationRule.ITEM_ID_NOT_NULL.getCode());
        }

        return Result.success();
    }
}
