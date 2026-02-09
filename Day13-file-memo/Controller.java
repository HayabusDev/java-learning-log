import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);

    private final Service service;

    public Controller(Service service){
        this.service = service;
    }

    public void launch(){
        showLaunchMenu();
        boolean isRunning = true;

        while (isRunning){
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 3);

            switch (userInput){
                case 1:
                    handleSaveMemo();
                    break;
                case 2:
                    handleLoadMemo();
                    break;
                case 3:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void showLaunchMenu(){
        System.out.println("=====================");
        System.out.println("メモアプリへようこそ！");
        System.out.println("1. 書き込み（保存）");
        System.out.println("2. データ読み込み");
        System.out.println("3. 終了");
        System.out.println("=====================");
    }

    private void handleSaveMemo(){
        System.out.println("メモしたいことを入力してください。");
        String text = sc.nextLine();

        while (true){
            boolean userYesNo = InputUtil.confirmYesNo("続けて入力しますか？(y / n)");
            if (userYesNo){
                System.out.println("メモしたいことを入力してください。");
                text += "\n" + sc.nextLine();
            }else {
                break;
            }
        }

        boolean canWrite = service.writeMemo(text);

        if (!canWrite){
            System.out.println("登録処理エラー、もう一度やり直してください。");
            return;
        }

        System.out.println("メモを保存しました。");
    }

    private void handleLoadMemo(){
        System.out.println("メモを読み込みます。");
        System.out.println(service.readMemo());
    }
}
