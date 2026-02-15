public class CashPayment extends Payment {
    @Override
    public String methodName() {
        return "Cash";
    }

    @Override
    public int calcFee(int amount) {
        //現金は手数料なし
        return 0;
    }

    @Override
    public String executeCore(int totalAmount) {
        return methodName() + ": " + totalAmount + " の支払いを承りました。";
    }
}
