package inventoryManagementSystem.controller;

import inventoryManagementSystem.domain.Item;
import inventoryManagementSystem.result.ErrorCodeLike;
import inventoryManagementSystem.result.Result;
import inventoryManagementSystem.service.StockService;
import inventoryManagementSystem.util.InputUtil;
import inventoryManagementSystem.valueObject.ItemId;
import inventoryManagementSystem.valueObject.LowStockThreshold;
import inventoryManagementSystem.valueObject.Quantity;
import inventoryManagementSystem.valueObject.ReorderPoint;

import java.util.List;
import java.util.Optional;

public class ItemController {

    private final StockService stockService;

    public ItemController(StockService stockService) {
        this.stockService = stockService;
    }

    public void launch(){
        printMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 6);

            switch (userInput) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleReceive();
                    break;
                case 3:
                    handleShip();
                    break;
                case 4:
                    handleList();
                    break;
                case 5:
                    handleDelete();
                    break;
                case 6:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void printMenu() {
        System.out.println("=====================");
        System.out.println("在庫管理システム");
        System.out.println("1. 商品登録");
        System.out.println("2. 入荷");
        System.out.println("3. 出荷");
        System.out.println("4. 登録商品一覧");
        System.out.println("5. 登録商品削除");
        System.out.println("6. 終了");
        System.out.println("=====================");
    }

    private void handleRegister() {
        String itemName = InputUtil.readNonEmptyString("商品名を入力してください。");
        int inputInitialQty = InputUtil.readIntInRange("初期入荷数を入力してください。", 1, Quantity.MAX_QUANTITY);
        int inputReorderPoint = InputUtil.readIntInRange("再発注を開始する在庫数を入力してください。", 1, inputInitialQty);
        int inputLST = InputUtil.readIntInRange("在庫不足アラートを出す基準数量を入力してください", 1, inputInitialQty);

        boolean userYesNO = InputUtil.confirmYesNo("商品名: " + itemName + " | " + "初期数: " + inputInitialQty + "\nを登録します。よろしいですか？(y / n)");
        if (!userYesNO) {
            System.out.println("登録作業を中止しました。");
            return;
        }

        Quantity initialQty = new Quantity(inputInitialQty);
        ReorderPoint reorderPoint = new ReorderPoint(new Quantity(inputReorderPoint));
        LowStockThreshold lowStockThreshold = new LowStockThreshold(new Quantity(inputLST));

        Result<ItemId> result = stockService.registerItem(itemName, initialQty, reorderPoint, lowStockThreshold);
        if (!result.isSuccess()) {
            printResultErrors(result);
            System.out.println("最初からやり直してください。");
            return;
        }

        System.out.println("商品名: " + itemName + " | " + "初期数: " + inputInitialQty +"登録しました。"
                + "\n登録ID: " + result.getData().getItemId());
    }

    private void handleReceive() {
        Optional<List<Item>> optionalItems = getItemList();
        if (optionalItems.isEmpty()) {
            System.out.println("入荷できる商品はありません");
            return;
        }

        List<Item> items = optionalItems.get();
        listItemWithNum(items);
        int userChoice = InputUtil.readIntInRange("入荷する商品を選択してください。", 1, items.size());
        Item selectedItem = items.get(userChoice - 1);
        int max = Quantity.MAX_QUANTITY - selectedItem.getQuantity().getQuantity();
        if (max == 0){
            System.out.println("これ以上入荷できません。");
            return;
        }
        int inputAmount = InputUtil.readIntInRange("入荷数を入力してください。", 1, max);

        boolean userYesNo = InputUtil.confirmYesNo("商品名: " + selectedItem.getName() + " を " + inputAmount + "個入荷します。よろしいですか？(y / n)");
        if (!userYesNo) {
            System.out.println("入荷処理を中止しました。");
            return;
        }

        Quantity amount = new Quantity(inputAmount);

        Result<Void> result = stockService.receiveStock(selectedItem.getId(), amount);
        if (!result.isSuccess()) {
            printResultErrors(result);
            System.out.println("最初からやり直してください。");
            return;
        }

        System.out.println("商品名:" + selectedItem.getName() + " を " + inputAmount + "個入荷しました。");
    }

    private void handleShip() {
        Optional<List<Item>> optionalItems = getItemList();
        if (optionalItems.isEmpty()) {
            System.out.println("出荷できる商品はありません。");
            return;
        }

        List<Item> items = optionalItems.get();
        listItemWithNum(items);
        int userChoice = InputUtil.readIntInRange("出荷する商品を選択してください。", 1, items.size());
        Item selectedItem = items.get(userChoice - 1);

        if (selectedItem.getQuantity().isZero()){
            System.out.println("在庫数が0です。出荷できません。");
            return;
        }

        int inputAmount = InputUtil.readIntInRange("出荷数を入力してください。", 1, selectedItem.getQuantity().getQuantity());

        boolean userYesNo = InputUtil.confirmYesNo("商品名: " + selectedItem.getName() + " を " + inputAmount + "個出荷します。よろしいですか？(y / n)");
        if (!userYesNo) {
            System.out.println("出荷処理を中止しました。");
            return;
        }

        Quantity amount = new Quantity(inputAmount);

        Result<Void> result = stockService.shipStock(selectedItem.getId(), amount);
        if (!result.isSuccess()) {
            printResultErrors(result);
            System.out.println("最初からやり直してください。");
            return;
        }

        System.out.println("商品名: " + selectedItem.getName() + " を " + inputAmount + "個出荷しました");
    }

    private void handleList() {
        Optional<List<Item>> optionalItems = getItemList();

        if (optionalItems.isEmpty()) {
            System.out.println("登録商品はありません。");
            return;
        }

        listItemWithNum(optionalItems.get());
    }

    private void handleDelete() {
        Optional<List<Item>> optionalItems = getItemList();
        if (optionalItems.isEmpty()) {
            System.out.println("削除できる商品はありません");
            return;
        }

        List<Item> items = optionalItems.get();
        listItemWithNum(items);
        int userChoice = InputUtil.readIntInRange("削除する商品を選択してください。", 1, items.size());
        Item selectedItem = items.get(userChoice - 1);

        boolean userYesNo = InputUtil.confirmYesNo("商品名: " + selectedItem.getName() + " を削除します。よろしいですか？(y / n)");
        if (!userYesNo) {
            System.out.println("削除処理を中止しました。");
            return;
        }

        Result<Void> result = stockService.deleteItem(selectedItem.getId());
        if (!result.isSuccess()) {
            printResultErrors(result);
            System.out.println("最初からやり直してください。");
            return;
        }

        System.out.println("商品名: " + selectedItem.getName() + " を削除しました。");
    }

    private void printResultErrors(Result<?> can) {
        for (ErrorCodeLike errorCode : can.getErrorCodeLikes()) {
            System.out.println(errorCode.getMessage());
        }
    }

    private Optional<List<Item>> getItemList(){
        Result<List<Item>> itemListResult = stockService.listItems();

        if (!itemListResult.isSuccess()) {
            printResultErrors(itemListResult);
            return Optional.empty();
        }

        List<Item> itemList = itemListResult.getData();

        if (itemList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(itemList);
    }

    private void listItemWithNum(List<Item> itemList){
        System.out.println("全" + itemList.size() + "件");
        for (int i = 0; i < itemList.size(); i++){
            System.out.println(i+1 + ": " + itemList.get(i));
        }
    }
}
