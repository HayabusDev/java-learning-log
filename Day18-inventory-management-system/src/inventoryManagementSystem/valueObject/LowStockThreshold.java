package inventoryManagementSystem.valueObject;

public class LowStockThreshold {

    private final Quantity threshold;

    public LowStockThreshold(Quantity threshold) {
        if (threshold == null) {
            throw new IllegalArgumentException("threshold cannot be null");
        }

        this.threshold = threshold;
    }

    //デバッグ用に残す
    public Quantity getLowStockThreshold() {
        return threshold;
    }

    public boolean isLow(Quantity quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("quantity cannot be null");
        }

        if  (this.threshold.getQuantity() >= quantity.getQuantity()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof LowStockThreshold)) {
            return false;
        }

        LowStockThreshold otherLowStockThreshould = (LowStockThreshold) other;
        if (this.threshold.equals(otherLowStockThreshould.threshold)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.threshold.hashCode();
    }

    @Override
    public String toString(){
        return String.valueOf(this.threshold);
    }
}
