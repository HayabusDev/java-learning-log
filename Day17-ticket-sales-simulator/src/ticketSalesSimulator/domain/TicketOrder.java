package tikectSalesSimulator.domain;

import java.util.UUID;

public class TicketOrder {
    private final String buyerName;
    private final UUID orderId;
    private final UUID eventId;
    private final int purchaseQuantity;
    private OrderStatus orderStatus;

    public TicketOrder(String buyerName, UUID orderId, UUID eventId, int purchaseQuantity, OrderStatus orderStatus){
        this.buyerName = buyerName;
        this.orderId = orderId;
        this.eventId = eventId;
        this.purchaseQuantity = purchaseQuantity;
        this.orderStatus = orderStatus;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public boolean canCancel(){
        return this.orderStatus.canCancel();
    }

    public void changeToCancelStatus(){
        if (!canCancel()){
            return;
        }
        this.orderStatus = OrderStatus.CANCELED;
    }
}
