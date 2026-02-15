public class Controller {

    private final Service service;
    private final PaymentFactory paymentFactory;

    public Controller(Service service, PaymentFactory paymentFactory){
        this.service = service;
        this.paymentFactory = paymentFactory;
    }

    public void launch() {
        showLaunchMenu();
        int userChoice = InputUtil.readIntInRange("決済方法を入力してください。(1 ~ 3)", 1, 3);
        int maxCharge = 1000000;
        int charge = InputUtil.readIntInRange("支払金額を入力してください。", 1, maxCharge);

        boolean userYesNo = InputUtil.confirmYesNo(paymentChoice(userChoice) + " の決済で " + charge + " 額の決済をします。よろしいですか？(y / n)");

        if (!userYesNo){
            System.out.println("決済をキャンセルしました。");
            return;
        }

        Receipt receipt = service.processPayment(paymentFactory.createPayment(userChoice), charge);
        System.out.println(receipt.getMessage());
    }

    private void showLaunchMenu(){
        System.out.println("=====================");
        System.out.println("簡単決済システム");
        System.out.println("1. クレジットカード");
        System.out.println("2. 現金");
        System.out.println("3. QRコード決済");
        System.out.println("=====================");
    }

    private String paymentChoice(int userChoice){
        if (userChoice == 1) {
            return "クレジットカード";
        }else if (userChoice == 2) {
            return "現金";
        }else if (userChoice == 3)
            return "QRコード決済";
        throw new IllegalArgumentException("入力値が不正です。");
    }
}
