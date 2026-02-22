package inventoryManagementSystem.service;

import inventoryManagementSystem.domain.Item;
import inventoryManagementSystem.repository.ItemRepository;
import inventoryManagementSystem.result.Result;
import inventoryManagementSystem.result.SystemErrorCode;
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
                //TODO ItemErrorCode 作成後、変更する
                return Result.failure(SystemErrorCode.DATA_DUPLICATE);
            }
        }
        return Result.success();
    }

    public Result<Void> receiveStock (ItemId itemId, Quantity amount) {
        //入力チェック Validator(itemId, amount(健全性（null / 0以下）含む))

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

        //存在チェック
        Result<Item> itemResult = isExist(itemId);
        if (!itemResult.isSuccess()) {
            return Result.failure(itemResult.getErrorCodeLikes());
        }

        Item shippingItem = itemResult.getData();

        //amount条件チェック
        if (shippingItem.getQuantity().isLessThan(amount)) {
            //TODO ItemErrorCode 作成後、変更する (INSUFFICIENT_STOCK)
            return Result.failure(SystemErrorCode.OPERATION_NOT_ALLOWED);
        }

        shippingItem.ship(amount);
        return Result.success();
    }

    public Result<Void> deleteItem (ItemId itemId) {
        //入力チェック Validator(itemId)

        //存在チェック
        Result<Item> itemResult = isExist(itemId);
        if (!itemResult.isSuccess()) {
            return Result.failure(itemResult.getErrorCodeLikes());
        }

        itemRepository.deleteById(itemId);
        return Result.success();
    }

    private Result<Item> isExist (ItemId itemId) {
        //入力チェック Validator(itemId)

        Item item = itemRepository.findById(itemId);
        if (item == null) {
            //TODO ItemErrorCode 作成後、変更する
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
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
