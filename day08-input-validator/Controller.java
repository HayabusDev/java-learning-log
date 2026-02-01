import java.text.Normalizer;
import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);

    public void launch(){
        String userName = readNonEmptyString("名前を入力してください。");
        System.out.println("ようこそ！" + userName +"さん！");

        int userAge = readIntInRange("年齢を入力してください。(0 ~ 120)", 0 ,120);
        System.out.println(userAge + "!");

        boolean isLiveJP = confirmYesNo("日本に住んでいますか？ (y/n)");
        if (isLiveJP){
            System.out.println("いいですね！");
        }else {
            System.out.println("いつか日本に来てください！");
        }
        String userInput = readNonEmptyString("どこに住んでますか？");
        System.out.println(userInput + "!");
    }

    private String readNonEmptyString(String question){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();
            if (userInput.isBlank()){
                System.out.println("入力が空です。");
            }else {
                break;
            }
        }
        return userInput;
    }

    private boolean confirmYesNo(String question){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();

            String normalizedUserInput = Normalizer.normalize(userInput, Normalizer.Form.NFKC);
            String finalizedUserInput = normalizedUserInput.toUpperCase();

            //下記2種は毎回new クラス定数化を検討
            String[] yChoices = {"Y", "YES", "はい"};
            String[] nChoices = {"N", "NO", "いいえ"};

            for (String yChoice : yChoices) {
                if (finalizedUserInput.equals(yChoice)) {
                    return true;
                }
            }

            for (String nChoice : nChoices){
                if (finalizedUserInput.equals(nChoice)){
                    return false;
                }
            }

            System.out.println("y か n で入力してください。");
        }
    }

    private int readIntInRange(String question, int min, int max){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();
            String normalizedUserInput = Normalizer.normalize(userInput, Normalizer.Form.NFKC);
            try{
                int convertedToInt = Integer.parseInt(normalizedUserInput);
                if (convertedToInt < min || max < convertedToInt){
                    System.out.println("入力値が範囲外です。");
                }else {
                    return convertedToInt;
                }
            }catch (NumberFormatException e){
                System.out.println("入力値が不正です。");
            }
        }
    }
}
