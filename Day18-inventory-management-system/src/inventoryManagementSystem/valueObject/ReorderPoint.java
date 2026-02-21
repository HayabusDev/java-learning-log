package inventoryManagementSystem.valueObject;

public class ReorderPoint {

    private final Quantity point;

    public ReorderPoint(Quantity point) {
        if (point == null) {
            throw new IllegalArgumentException("point cannot be null");
        }

        this.point = point;
    }

    //デバッグ用
    public Quantity getPoint() {
        return point;
    }

    public boolean needsReorder(Quantity quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("quantity cannot be null");
        }

        if (this.point.getQuantity() >= quantity.getQuantity()) {
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

        if (!(other instanceof ReorderPoint)) {
            return false;
        }

        ReorderPoint otherPoint = (ReorderPoint) other;
        if (this.point.equals(otherPoint.point)){
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.point.hashCode();
    }
}
