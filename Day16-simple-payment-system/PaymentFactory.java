public class PaymentFactory {

    public Payment createPayment(int userChoice){
        //InputUtil が 1〜3 を保証する
        if (userChoice == 1) {
            return new CreditCardPayment();
        }else if (userChoice == 2) {
            return new CashPayment();
        }else if (userChoice == 3)
            return new QRPayment();
        throw new IllegalArgumentException("支払い情報が不正です。");
    }
}
