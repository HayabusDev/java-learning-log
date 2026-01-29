public class Account {
    private final int id;
    private final String userPassword;
    private double balance;

    Account(int id, String userPassword, double balance){
        this.id = id;
        this.userPassword = userPassword;
        this.balance = balance;
    }

    public boolean deposit(double depositBalance) {
        if (depositBalance <= 0){
            return false;
        }else {
            balance = balance + depositBalance;
            return true;
        }
    }

    public boolean withdraw(double withdrawBalance) {
        if (withdrawBalance <= 0){
            return false;
        }else if(balance < withdrawBalance){
            return false;
        }else {
            balance = balance - withdrawBalance;
            return true;
        }
    }

    private String getUserPassword() { return userPassword; }

    public int getId() { return id; }

    public double getBalance(){ return balance;}
}
