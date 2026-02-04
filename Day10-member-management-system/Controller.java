import java.util.List;

public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public void launch() {
        showLaunchMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 4);

            switch (userInput) {
                case 1:
                    handleAddUser();
                    break;
                case 2:
                    handleSearchUser();
                    break;
                case 3:
                    handleDeleteUser();
                    break;
                case 4:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void showLaunchMenu() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("会員管理システム");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("1. 登録");
        System.out.println("2. 検索");
        System.out.println("3. 削除");
        System.out.println("4. 終了");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆");
    }

    private void handleAddUser() {
        String userId = InputUtil.readNonEmptyString("登録するユーザー名を入力してください。");
        //UX重視、確認前にエラーが出る設計のため二重チェック
        Result<Void> result = service.validateAddUser(userId);
        if (!result.isSuccess()) {
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        boolean userYesNo = InputUtil.confirmYesNo(userId + " を登録します。よろしいですか？(y / n)");
        if (!userYesNo) {
            System.out.println("登録をキャンセルしました。再登録する場合は最初からやり直してください。");
            return;
        }

        Result<Void> canAddResult = service.addUser(userId);
        if (!canAddResult.isSuccess()) {
            System.out.println(canAddResult.getMessage());
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println(userId + " を登録しました。");
    }

    private void handleSearchUser(){
        String keyword = InputUtil.readNonEmptyString("検索キーワードを入力してください。");
        Result<List<User>> result = service.searchUser(keyword);
        if (!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<User> users = result.getData();
        if (users.isEmpty()){
            System.out.println("見つかりませんでした。");
            return;
        }

        System.out.println(users.size() + " 件見つかりました。");
        for (User user : users){
            System.out.println(user.getUserId());
        }
    }

    private void handleDeleteUser(){
        String userId = InputUtil.readNonEmptyString("削除するユーザー名を入力してください。");
        //UX重視、確認前にエラーが出る設計のため二重チェック
        Result<Void> result = service.validateDeleteUser(userId);
        if(!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        boolean userYesNo = InputUtil.confirmYesNo(userId + " を削除します。よろしいですか？(y / n)");
        if(!userYesNo){
            System.out.println("削除をキャンセルしました。");
            return;
        }

        Result<Void> canDeleteResult = service.deleteUser(userId);
        if (!canDeleteResult.isSuccess()){
            System.out.println(canDeleteResult.getMessage());
            System.out.println("最初からやり直してください。");
            return;
        }
        System.out.println(userId + " を削除しました。");
    }
}
