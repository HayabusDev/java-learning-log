package tikectSalesSimulator.domain;

import java.util.UUID;

public class Event {
    private final String eventName;
    private final UUID eventId;
    private final int allSeats;
    private int remainSeats;
    private SaleStatus saleStatus;
    public static final int MAX_SEATS = 60000;

    public Event(String eventName, UUID eventId, SaleStatus saleStatus, int allSeats, int remainSeats){
        this.eventName = eventName;
        this.eventId = eventId;
        this.saleStatus = saleStatus;

        if (remainSeats < 0) {
            throw new IllegalArgumentException("remainSeats cannot be negative.");
        }
        if (allSeats <= 0){
            throw new IllegalArgumentException("allSeats cannot be negative.");
        }
        if (allSeats > MAX_SEATS){
            throw new IllegalArgumentException("allSeats cannot exceed MAX_SEATS.");
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

    public boolean purchase(int quantity){
        if (!canPurchase(quantity)){
            return false;
        }

        remainSeats = remainSeats - quantity;

        if (remainSeats == 0){
            changeStatus(SaleStatus.SOLD_OUT);
        }
        return true;
    }

    public void cancel(int quantity){
        if (!cancelable(quantity)){
            return;
        }

        remainSeats = remainSeats + quantity;

        if (this.saleStatus == SaleStatus.SOLD_OUT){
            changeStatus(SaleStatus.ON_SALE);
        }
    }

    public boolean cancelable(int quantity){
        if (quantity <= 0 || allSeats < remainSeats + quantity){
            return false;
        }

        if (!this.saleStatus.isCancelable()){
            return false;
        }

        return true;
    }

    private boolean canStartSale(){
        if (!this.saleStatus.canTransitTo(SaleStatus.ON_SALE)){
            return false;
        }
        return true;
    }

    private boolean canCloseSale(){
        if (!this.saleStatus.canTransitTo(SaleStatus.CLOSED)){
            return false;
        }
        return true;
    }

    public boolean startSale(){
        if (!canStartSale()){
            return false;
        }
        this.saleStatus = SaleStatus.ON_SALE;
        return true;
    }

    public boolean closeSale(){
        if (!canCloseSale()){
            return false;
        }
        this.saleStatus = SaleStatus.CLOSED;
        return true;
    }

    private boolean changeStatus(SaleStatus nextSaleStatus){
        if (nextSaleStatus == null){
            throw new IllegalArgumentException("nextSaleStatus must not be null");
        }

        if (!this.saleStatus.canTransitTo(nextSaleStatus)){
            return false;
        }

        this.saleStatus = nextSaleStatus;
        return true;
    }


    @Override
    public String toString(){
        return "イベントID: " + eventId + " | " + "イベント名: " + eventName + " | "
                + "販売状態: " + saleStatus + " | " + "残り販売数: " + remainSeats + " | "
                + "座席販売数: " + allSeats;
    }
}
