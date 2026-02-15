public class Main {
    public static void main(String[] args){

        Service service = new Service();
        PaymentFactory paymentFactory = new PaymentFactory();
        Controller controller = new Controller(service, paymentFactory);

        controller.launch();
    }
}
