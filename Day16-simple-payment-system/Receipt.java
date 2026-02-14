public class Receipt {

    private final String paymentMethod;
    private final int charge;
    private final int totalAmount;
    private final int paymentFee;

    public Receipt(String paymentMethod, int charge, int totalAmount, int paymentFee){
        this.paymentMethod = paymentMethod;
        this.charge = charge;
        this.totalAmount = totalAmount;
        this.paymentFee = paymentFee;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getCharge() {
        return charge;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPaymentFee() {
        return paymentFee;
    }
}
