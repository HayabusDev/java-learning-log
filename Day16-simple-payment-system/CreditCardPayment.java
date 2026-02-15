public class CreditCardPayment extends Payment {

    @Override
    public String methodName() {
        return "CreditCard";
    }

    @Override
    public int calcFee(int amount) {
        //手数料3%（端数は切り上げ）
        double serviceFee = 0.03;
        return (int) Math.ceil(amount * serviceFee);
    }

    @Override
    public String executeCore(int totalAmount) {
        return methodName() + ": " + totalAmount + " を支払いを承認します。決済確認中・・・";
    }
}
