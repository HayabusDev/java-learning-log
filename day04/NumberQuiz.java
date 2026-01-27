import java.util.Random;
import java.util.Scanner;

public class NumberQuiz {
    static void main(String[] args){
        System.out.println("数字当てゲームへようこそ！");
        System.out.println("僕が考えた0~100の数字を当ててね！");

        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int randomNumber = random.nextInt(101);
        int inputNumber = 1000;

        while(randomNumber != inputNumber){
            System.out.println("数字を入力してね！");
            try{
                inputNumber = sc.nextInt();
            }catch (Exception e){
                System.out.println("エラーだよ。");
                sc.nextLine();
                continue;
            }
            if (inputNumber < randomNumber){
                System.out.println("それよりかは大きいよ。");
            } else if (inputNumber > randomNumber) {
                System.out.println("それよりかは小さいよ。");
            }
        }

        System.out.println("せいかーい！よくわかったね！また挑戦してね！");
    }
}
