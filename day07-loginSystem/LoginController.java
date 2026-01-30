import java.util.Scanner;

public class LoginController {
    Scanner sc = new Scanner(System.in);

    private final Service service;
    public LoginController (Service service){
        this.service = service;
    }

    private boolean isRunning = true;

    public void launchMenu(){
        while (isRunning){
            System.out.println("★★★★★★★★★★★★★★");
            System.out.println("ログインシステムへようこそ。");
            System.out.println("★★★★★★★★★★★★★★");
            System.out.println("1. ログイン");
            System.out.println("2. 新規登録");
            System.out.println("3. 終了");
            System.out.println("★★★★★★★★★★★★★★");

            System.out.println("用件を入力してください。(1~3)");
            int userChoice;
            userChoice = sc.nextInt();
            sc.nextLine();

            switch (userChoice){
                case 1:
                    runLoginSystem();
                    break;
                case 2:
                    runRegisterSystem();
                    break;
                case 3:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    public void runLoginSystem(){
        String inputId;
        String inputPassword;

        System.out.println("ユーザー名を入力してください。");
        inputId = sc.nextLine();
        System.out.println("パスワードを入力してください。");
        inputPassword = sc.nextLine();
        boolean canLogin = service.loginUser(inputId, inputPassword);

        if (canLogin){
            System.out.println("ログイン成功。こんにちは " + inputId + " さん");
        }else{
            System.out.println("ユーザー名かパスワードが異なっています。もう一度やり直してください。");
        }
    }

    public void runRegisterSystem(){
        String inputId;
        String inputPassword;

        System.out.println("登録するユーザー名を入力してください。");
        inputId = sc.nextLine();
        boolean isAvailable = service.isAvailableUserId(inputId);

        if (!isAvailable){
            System.out.println("そのユーザー名は既に登録されてます。もう一度やり直してください。");
        }else {
            System.out.println("パスワードを登録してください。");
            inputPassword = sc.nextLine();
            boolean canRegister = service.registerUser(inputId, inputPassword);
            if (canRegister){
                System.out.println("登録完了！");
            }else {
                System.out.println("エラー: 登録できません。");
            }
        }
    }
}
