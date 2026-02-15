public class Service {

    public Service(){}

    public Receipt processPayment(Payment payment, int charge){
        return payment.pay(charge);
    }
}
