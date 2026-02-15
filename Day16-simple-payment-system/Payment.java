public abstract class Payment {

    public final Receipt pay(int charge){
        String paymentMethod = methodName();
        int paymentFee = calcFee(charge);
        int totalAmount = charge + paymentFee;
        String message = executeCore(totalAmount);
        return new Receipt(paymentMethod, charge, totalAmount, paymentFee, message);
    };

    public abstract String methodName();

    public abstract int calcFee(int amount);

    public abstract String executeCore(int totalAmount);
}
