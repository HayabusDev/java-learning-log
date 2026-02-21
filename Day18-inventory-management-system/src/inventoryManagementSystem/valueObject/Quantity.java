package inventoryManagementSystem.valueObject;

public class Quantity {

    private final int quantity;
    public Quantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isZero(){
        return this.quantity == 0;
    }

    //入荷
    public Quantity addQuantity(Quantity otherQuantity){
        if (otherQuantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        int addedQuantity = this.quantity + otherQuantity.getQuantity();
        return new Quantity(addedQuantity);
    }

    //出庫（可否判定は呼び出し側が行う）
    public Quantity subtractQuantity(Quantity otherQuantity){
        if (otherQuantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        int subtractedQuantity = this.quantity - otherQuantity.getQuantity();
        return new Quantity(subtractedQuantity);
    }

    public boolean isGreaterThan(Quantity otherQuantity){
        if (otherQuantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.quantity <= otherQuantity.getQuantity()) {
            return false;
        }
        return true;
    }

    public boolean isLessThan(Quantity otherQuantity){
        if (otherQuantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.quantity >= otherQuantity.getQuantity()) {
            return false;
        }
        return true;
    }

    public boolean isEqual(Quantity otherQuantity){
        if (otherQuantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.quantity != otherQuantity.getQuantity()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof Quantity)) {
            return false;
        }

        Quantity otherQuantity = (Quantity) other;
        if (this.quantity == otherQuantity.quantity) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.quantity);
    }

    @Override
    public String toString() {
        return String.valueOf(this.quantity);
    }
}
