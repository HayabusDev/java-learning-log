public class Order {
    private int orderId;
    private DrinkType drinkType;
    private int quantity;

    public Order(int orderId, DrinkType drinkType, int quantity){
        this.orderId = orderId;
        this.drinkType = drinkType;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public DrinkType getDrinkType() {
        return drinkType;
    }

    @Override
    public String toString(){
        return "[" + orderId + "]" + " ドリンク: " + drinkType + " | " + " 個数: " + quantity;
    }
}
