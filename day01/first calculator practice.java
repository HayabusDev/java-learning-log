import java.util.Scanner;

public class Calculator {
    public static void main(String[] args){
        System.out.println("簡易コンソール電卓へようこそ！");
        Scanner sc = new Scanner(System.in);

        String calc;
        while (true){
            System.out.println("演算子を入力してください。 (+, -, *, /) : ");
            calc = sc.nextLine().trim();
            if (calc.equals("+") || calc.equals("-") || calc.equals("*") || calc.equals("/")){
                break;
            }
            System.out.println("エラー: 演算子を正しく入力してください。");
        }

        Double num1;
        while (true){
            System.out.println("最初の数字を入力してください。");
            try {
                num1 = sc.nextDouble();
                break;
            }catch (Exception e){
                System.out.println("エラー: 数字を入力してください。");
                sc.nextLine();
            }
        }

        Double num2;
        while (true){
            System.out.println("次の数字を入力してください。");
            try {
                num2 = sc.nextDouble();
                break;
            }catch (Exception e) {
                System.out.println("エラー: 数字を入力してください。");
                sc.nextLine();
            }
        }

        Double result = (double) 0;

        switch (calc){
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0){
                    System.out.println("エラー: 0で割ることはできません。");
                    return;
                }
                result = num1 / num2;
                break;
            default:
        }

        System.out.println("計算結果: " + result);
    }
}
