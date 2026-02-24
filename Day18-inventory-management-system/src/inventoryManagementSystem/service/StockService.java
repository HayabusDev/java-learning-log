package inventoryManagementSystem.service;

import inventoryManagementSystem.domain.Item;
import inventoryManagementSystem.repository.ItemRepository;
import inventoryManagementSystem.result.ItemErrorCode;
import inventoryManagementSystem.result.Result;
import inventoryManagementSystem.validator.ItemValidator;
import inventoryManagementSystem.valueObject.ItemId;
import inventoryManagementSystem.valueObject.LowStockThreshold;
import inventoryManagementSystem.valueObject.Quantity;
import inventoryManagementSystem.valueObject.ReorderPoint;

import java.util.*;

public class StockService {
    //入力チェックはValidator設計後に実装
    //SafeAction は全体完成後に実装

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    public StockService(ItemRepository itemRepository, ItemValidator itemValidator) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
    }

    public Result<ItemId> registerItem (String itemName, Quantity initialQty, ReorderPoint reorderPoint, LowStockThreshold lowStockThreshold) {
        //Validator(itemName, initialQty, reorderPoint, lowStockThreshold)
        Result<Void> validate = itemValidator.validateRegister(itemName, initialQty, reorderPoint, lowStockThreshold);
        if (!validate.isSuccess()) {
            return Result.failure(validate.getErrorCodeLikes());
        }

        //重複チェック(名前重複禁止)
        Result<Void> isExist = checkDuplicateItemName(itemName);
        if (!isExist.isSuccess()) {
            return Result.failure(isExist.getErrorCodeLikes());
        }

        ItemId itemId = new ItemId(UUID.randomUUID());
        Item createdItem = new Item(itemId, itemName, initialQty, reorderPoint, lowStockThreshold);
        itemRepository.save(createdItem);
        return Result.success(createdItem.getId());
    }

    private Result<Void> checkDuplicateItemName (String itemName) {
        Map<ItemId, Item> existingItems = itemRepository.findAll();
        for (Item  item : existingItems.values()) {
            if (Objects.equals(item.getName(), itemName)) {
                return Result.failure(ItemErrorCode.ITEM_NAME_DUPLICATE);
            }
        }
        return Result.success();
    }

    public Result<Void> receiveStock (ItemId itemId, Quantity amount) {
        //入力チェック Validator(itemId, amount(健全性（null / 0以下）含む))
        Result<Void> validate = itemValidator.validateReceiveAndShip(itemId, amount);
        if (!validate.isSuccess()) {
            return Result.failure(validate.getErrorCodeLikes());
        }

        //存在チェック
        Result<Item> itemResult = isExist(itemId);
        if (!itemResult.isSuccess()) {
            return Result.failure(itemResult.getErrorCodeLikes());
        }

        Item arrivalItem = itemResult.getData();
        arrivalItem.receive(amount);
        return Result.success();
    }

    public Result<Void> shipStock (ItemId itemId, Quantity amount) {
        //入力チェック Validator(itemId, amount(健全性（null / 0以下）含む))
        Result<Void> validate = itemValidator.validateReceiveAndShip(itemId, amount);
        if (!validate.isSuccess()) {
            return Result.failure(validate.getErrorCodeLikes());
        }

        //存在チェック
        Result<Item> itemResult = isExist(itemId);
        if (!itemResult.isSuccess()) {
            return Result.failure(itemResult.getErrorCodeLikes());
        }

        Item shippingItem = itemResult.getData();

        //amount条件チェック
        if (shippingItem.getQuantity().isLessThan(amount)) {
            return Result.failure(ItemErrorCode.INSUFFICIENT_STOCK);
        }

        shippingItem.ship(amount);
        return Result.success();
    }

    public Result<Void> deleteItem (ItemId itemId) {
        //入力チェック Validator(itemId)
        Result<Void> validate = itemValidator.validateItemId(itemId);
        if (!validate.isSuccess()) {
            return Result.failure(validate.getErrorCodeLikes());
        }

        //存在チェック
        Result<Item> itemResult = isExist(itemId);
        if (!itemResult.isSuccess()) {
            return Result.failure(itemResult.getErrorCodeLikes());
        }

        itemRepository.deleteById(itemId);
        return Result.success();
    }

    private Result<Item> isExist (ItemId itemId) {
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            return Result.failure(ItemErrorCode.ITEM_DATA_NOT_FOUND);
        }
        return Result.success(item);
    }

    public Result<List<Item>> listItems (){
        //空なら成功、空リストを返す
        return Result.success(convertToList());
    }

    //MapをListに変換
    private List<Item> convertToList (){
        return new ArrayList<>(itemRepository.findAll().values());
    }
}
