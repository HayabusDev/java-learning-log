import java.util.Scanner;

public class KukuCalculator {
    public static void main(String[] args) {
        System.out.println("九九アプリへようこそ！");

        Scanner sc = new Scanner(System.in);
        boolean isContinue = true;

        while (isContinue){

            int userInput;
            String userChoice;

            while (true) {
                System.out.println("1~9の段の数字を入力してね！");
                try {
                    userInput = sc.nextInt();
                    sc.nextLine();
                } catch (Exception e) {
                    System.out.println("エラー: 1~9の数字を入力してね！");
                    sc.nextLine();
                    continue;
                }
                if (userInput > 9 || userInput < 1) {
                    System.out.println("エラー: 1~9の数字でお願いね！");
                } else {
                    break;
                }
            }

            for (int i = 1; i < 10; i++) {
                int result;
                result = userInput * i;
                System.out.println(userInput +" x " + i + " = " + result);
            }

            while (true){
                System.out.println("継続する？ 続けるなら y を、やめるなら q　を入力してね。");
                userChoice = sc.nextLine().trim();
                if (userChoice.equals("y") || userChoice.equals("q")){
                    break;
                }else{
                    System.out.println("y か q でお願いね！");
                    continue;
                }
            }
            if (userChoice.equals("y")){
                continue;
            }else {
                isContinue = false;
            }
        }

        sc.close();
    }
}
