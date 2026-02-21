package inventoryManagementSystem.domain;

public enum StockStatus {
    OUT_OF_STOCK, //数量=0
    NEED_REORDER, //数量 <= reorderPoint（発注すべき）
    LOW_STOCK, //数量 <= lowStockThreshold（少ない）
    OK //正常
}
