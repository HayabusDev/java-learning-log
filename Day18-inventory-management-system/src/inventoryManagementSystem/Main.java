package inventoryManagementSystem;

import inventoryManagementSystem.controller.ItemController;
import inventoryManagementSystem.repository.ItemRepository;
import inventoryManagementSystem.service.StockService;
import inventoryManagementSystem.validator.ItemValidator;

public class Main {
    public static void main(String[] args){
        ItemRepository itemRepository = new ItemRepository();
        ItemValidator itemValidator = new ItemValidator();
        StockService stockService = new StockService(itemRepository, itemValidator);
        ItemController itemController = new ItemController(stockService);

        itemController.launch();
    }
}
