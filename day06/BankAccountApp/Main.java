import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int userChoice;
        boolean isRunning = true;

        Account defaultAccount = new Account(1234, "abc123", 0.0);

        while (isRunning) {

            System.out.println("*******************************************");
            System.out.println("おはようございます。銀行口座管理システムへようこそ。");
            System.out.println("*******************************************");
            System.out.println("1. 残高確認");
            System.out.println("2. 入金");
            System.out.println("3. 出金");
            System.out.println("4. 口座作成");
            System.out.println("5. 終了");
            System.out.println("*******************************************");

            // User Choice
            System.out.println("本日のご要件は何でしょう？(1~5を入力してください。)");
            userChoice = sc.nextInt();
            sc.nextLine();

            switch (userChoice) {
                case 1:
                    System.out.println("残高: " + defaultAccount.getBalance());
                    break;
                case 2:
                    System.out.println("入金額を入力してください。");
                    double depositBalance = sc.nextDouble();
                    sc.nextLine();
                    if (defaultAccount.deposit(depositBalance)){
                        System.out.println(depositBalance + " 入金しました。");
                        System.out.println("残高: " + defaultAccount.getBalance());
                    }else{
                        System.out.println("エラー:1 入力金額は入金できません。最初からやり直してください。");
                    }
                    break;
                case 3:
                    System.out.println("出金額を入力してください。");
                    double withdrawBalance = sc.nextDouble();
                    sc.nextLine();
                    if (defaultAccount.withdraw(withdrawBalance))
                    {
                        System.out.println(withdrawBalance + " 出金しました。");
                        System.out.println("残高: " + defaultAccount.getBalance());
                    }else{
                        System.out.println("エラー:2　入力金額は出金できません。最初からやり直してください。");
                    }
                    break;
                case 4 :
                    System.out.println("口座作成");
                    break;
                case 5:
                    System.out.println("終了");
                    isRunning = false;
                    break;
                default:
            }
        }

        System.out.println("ご利用ありがとうございました。");

        sc.close();
    }
}
