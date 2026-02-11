import java.util.List;
import java.util.Objects;

public class Controller {

    private final Service service;

    public Controller(Service service){
        this.service = service;
    }

    public void launch(){
        showLaunchMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 4);

            switch (userInput) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    handleList();
                    break;
                case 4:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void showLaunchMenu(){
        System.out.println("=====================");
        System.out.println("ログイン認証システム");
        System.out.println("1. ログイン");
        System.out.println("2. 登録");
        System.out.println("3. ユーザー一覧");
        System.out.println("4. 終了");
        System.out.println("=====================");
    }

    private void handleLogin(){
        String inputUserId = InputUtil.readNonEmptyString("ユーザーIDを入力してください。");
        String inputPassword = InputUtil.readNonEmptyString("パスワードを入力してください。");

        Result<Void> canLogin = service.login(inputUserId, inputPassword);

        if (!canLogin.isSuccess()){
            printResultErrors(canLogin);
            System.out.println("ログインできません。");
            return;
        }

        System.out.println("ようこそ！" + inputUserId + " さん！");
    }

    private void handleRegistration(){
        String inputUserId = InputUtil.readNonEmptyString("ユーザーIDを入力してください。");
        String inputPassword = InputUtil.readNonEmptyString("パスワードを入力してください。");
        String inputPasswordCheck = InputUtil.readNonEmptyString("確認のため、もう一度パスワードを入力してください。");

        if (!Objects.equals(inputPassword, inputPasswordCheck)){
            System.out.println("パスワードが一致しません。最初からやり直してください。");
            return;
        }

        Result<Void> canRegister = service.register(inputUserId, inputPassword);

        if (!canRegister.isSuccess()){
            printResultErrors(canRegister);
            System.out.println("登録できません。最初からやり直してください。");
            return;
        }

        System.out.println(inputUserId + " を登録しました。");
    }

    private void printResultErrors(Result<?> can){
        for (ErrorCodeLike errorCode : can.getErrorCodeLikes()){
            System.out.println(errorCode.getMessage());
        }
    }

    private void handleList(){
        Result<List<User>> userListResult = service.listUser();

        if(!userListResult.isSuccess()){
            printResultErrors(userListResult);
            return;
        }

        List<User> userList = userListResult.getData();

        if (userList.isEmpty()){
            System.out.println("登録ユーザーはありません。");
            return;
        }

        System.out.println("全 " + userList.size() + " 件");

        for(User user : userList){
            System.out.println(user);
        }
    }
}
