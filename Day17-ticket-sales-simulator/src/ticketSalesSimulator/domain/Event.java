package tikectSalesSimulator.domain;

import java.util.UUID;

public class Event {
    private final String eventName;
    private final UUID eventId;
    private final int allSeats;
    private int remainSeats;
    private SaleStatus saleStatus;

    public Event(String eventName, UUID eventId, SaleStatus saleStatus, int allSeats, int remainSeats){
        this.eventName = eventName;
        this.eventId = eventId;
        this.saleStatus = saleStatus;

        if (remainSeats < 0) {
            throw new IllegalArgumentException("remainSeats cannot be negative.");
        }
        if (allSeats < remainSeats) {
            throw new IllegalArgumentException("remainSeats cannot exceed allSeats.");
        }

        this.allSeats = allSeats;
        this.remainSeats = remainSeats;
    }

    public String getEventName() {
        return eventName;
    }

    public SaleStatus getSaleStatus() {
        return saleStatus;
    }

    public int getAllSeats() {
        return allSeats;
    }

    public int getRemainSeats() {
        return remainSeats;
    }

    public UUID getEventId() {
        return eventId;
    }

    public boolean canPurchase(int quantity){
        if (!this.saleStatus.isPurchasable()){
            return false;
        }

        if (quantity <= 0 || remainSeats < quantity){
            return false;
        }

        return true;
    }

    public void purchase(int quantity){
        if (!canPurchase(quantity)){
            return;
        }

        remainSeats = remainSeats - quantity;

        if (remainSeats == 0){
            changeStatus(SaleStatus.SOLD_OUT);
        }
    }

    public void cancel(int quantity){
        if (quantity <= 0 || allSeats < remainSeats + quantity){
            return;
        }

        if (!this.saleStatus.isCancelable()){
            return;
        }

        remainSeats = remainSeats + quantity;

        if (this.saleStatus == SaleStatus.SOLD_OUT){
            changeStatus(SaleStatus.ON_SALE);
        }
    }

    public boolean canStartSale(){
        if (!this.saleStatus.canTransitTo(SaleStatus.ON_SALE)){
            return false;
        }

        if (allSeats <= 0){
            return false;
        }
        return true;
    }

    public void changeStatus(SaleStatus nextSaleStatus){
        if (!this.saleStatus.canTransitTo(nextSaleStatus)){
            return;
        }

        this.saleStatus = nextSaleStatus;
    }
}
