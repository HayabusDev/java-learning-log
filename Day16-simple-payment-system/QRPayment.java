public class QRPayment extends Payment{

    @Override
    public String methodName() {
        return "QRPayment";
    }

    @Override
    public int calcFee(int amount) {
        //固定30円 + 1%(端数切り上げ)
        double serviceFee = 0.01;
        int fixedFee = 30;
        return (int) Math.ceil(amount * serviceFee) + fixedFee;
    }

    @Override
    public String executeCore(int totalAmount) {
        return methodName() + ": " + totalAmount + " の支払いを承認します。決済情報を送信して確認中・・・";
    }
}
