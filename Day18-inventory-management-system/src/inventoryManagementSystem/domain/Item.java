package inventoryManagementSystem.domain;

import inventoryManagementSystem.valueObject.ItemId;
import inventoryManagementSystem.valueObject.LowStockThreshold;
import inventoryManagementSystem.valueObject.Quantity;
import inventoryManagementSystem.valueObject.ReorderPoint;

public class Item {
    private final ItemId id;
    private final String name;
    private Quantity quantity;
    private final ReorderPoint reorderPoint;
    private final LowStockThreshold lowStockThreshold;

    public Item(ItemId id, String name, Quantity quantity, ReorderPoint reorderPoint, LowStockThreshold lowStockThreshold) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (quantity == null) throw new IllegalArgumentException("quantity cannot be null");
        if (reorderPoint == null) throw new IllegalArgumentException("reorderPoint cannot be null");
        if (lowStockThreshold == null) throw new IllegalArgumentException("lowStockThreshold cannot be null");

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reorderPoint = reorderPoint;
        this.lowStockThreshold = lowStockThreshold;

        if (lowStockThreshold.getLowStockThreshold().getQuantity() < reorderPoint.getPoint().getQuantity()) {
            throw new IllegalArgumentException("lowStockThreshold should be greater than reorderPoint");
        }
    }

    public ItemId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public ReorderPoint getReorderPoint() {
        return reorderPoint;
    }

    public LowStockThreshold getLowStockThreshold() {
        return lowStockThreshold;
    }

    //状態算出
    private boolean needsReorder() {
        return this.quantity.getQuantity() <= reorderPoint.getPoint().getQuantity();
    }

    private boolean isLowStock(){
        return this.quantity.getQuantity() <= lowStockThreshold.getLowStockThreshold().getQuantity();
    }

    public StockStatus getStockStatus(){
        if (this.quantity.getQuantity() == 0){
            return StockStatus.OUT_OF_STOCK;
        }
        if (needsReorder()){
            return StockStatus.NEED_REORDER;
        }
        if (isLowStock()){
            return StockStatus.LOW_STOCK;
        }
        return StockStatus.OK;
    }

    //在庫変動 (可否判定はService, 計算だけ)
    public void receive(Quantity amount) {
        if (amount == null) throw new IllegalArgumentException("amount cannot be null");
        if (amount.getQuantity() <= 0) throw new IllegalArgumentException("amount must be positive");

        this.quantity = this.quantity.addQuantity(amount);
    }

    public void ship(Quantity amount) {
        if (amount == null) throw new IllegalArgumentException("amount cannot be null");
        if (amount.getQuantity() <= 0) throw new IllegalArgumentException("amount must be positive");

        this.quantity = this.quantity.subtractQuantity(amount);
    }

    @Override
    public String toString(){
        return "商品ID: " + id + " | " + "商品名: " + name + " | "
                + "在庫数: " + quantity + " | " + "発注点: " + reorderPoint + " | "
                + "基準数: " + lowStockThreshold;
    }
}
