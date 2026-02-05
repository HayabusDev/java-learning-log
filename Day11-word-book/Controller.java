import java.util.List;

public class Controller {
    private final Service service;

    public Controller(Service service){
        this.service = service;
    }

    public void launch(){
        showLaunchMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 5);

            switch (userInput) {
                case 1:
                    handleAddWord();
                    break;
                case 2:
                    handleListWord();
                    break;
                case 3:
                    handleSearchWord();
                    break;
                case 4:
                    handleDeleteWord();
                    break;
                case 5:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
                }
            }
    }

    private void showLaunchMenu(){
        System.out.println("=====================");
        System.out.println("単語帳アプリへようこそ！");
        System.out.println("1. 追加");
        System.out.println("2. 一覧");
        System.out.println("3. 英単語検索");
        System.out.println("4. 削除");
        System.out.println("5. 終了");
        System.out.println("=====================");
    }

    private void handleAddWord(){
        String addWord = InputUtil.readNonEmptyString("登録する英単語を入力してください。");
        String meaning = InputUtil.readNonEmptyString("英単語の意味を入力してください。");
        //UX重視、y/n確認前にエラーが出る設計のため二重チェック
        Result<Void> result = service.validateCreateWord(addWord, meaning);
        if (!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        boolean userYesNo = InputUtil.confirmYesNo(addWord + ": " + meaning + " を登録します。\nよろしいですか？(y / n)");
        if (!userYesNo){
            System.out.println("登録をキャンセルしました。再登録する場合は最初からやり直してください。");
            return;
        }

        Result<Void> canCreateWord = service.createWord(addWord, meaning);
        if(!canCreateWord.isSuccess()){
            System.out.println(canCreateWord.getMessage() + "\n最初からやり直してください。");
            return;
        }

        System.out.println(addWord + ": " + meaning + " を登録しました。");
    }


    private void handleListWord(){
        Result<List<Words>> result = service.listWord();
        if (!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<Words> wordsList = result.getData();

        if (wordsList.isEmpty()){
            System.out.println("登録されている単語はありません。");
            return;
        }

        System.out.println("全 " + wordsList.size() + " 件");

        for (Words word : wordsList){
            System.out.println(word);
        }
    }


    private void handleSortWord(){
        //TODO 2つ以上のソート項目を作成したときに実装する
        throw new UnsupportedOperationException("未実装");
    }

    private void handleSearchWord(){
        String keyWord = InputUtil.readNonEmptyString("検索キーワードを入力してください。");
        Result<List<Words>> result = service.searchWord(keyWord);
        if (!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<Words> words = result.getData();
        if (words.isEmpty()){
            System.out.println("見つかりませんでした。");
            return;
        }

        System.out.println(words.size() + " 件見つかりました。");
        for (Words word : words){
            System.out.println(word);
        }
    }

    private void handleDeleteWord(){
        String deleteWord = InputUtil.readNonEmptyString("削除する英単語を入力してください。");
        //UX重視、y/n確認前にエラーが出る設計のため二重チェック
        Result<Void> result = service.validateDeleteWord(deleteWord);
        if (!result.isSuccess()){
            System.out.println(result.getMessage() + "\n最初からやり直してください。");
            return;
        }

        boolean userYesNo = InputUtil.confirmYesNo(deleteWord + " を削除します。\nよろしいですか？(y / n)");
        if (!userYesNo){
            System.out.println("削除をキャンセルしました。再実行する場合は最初からやり直してください。");
            return;
        }

        Result<Void> canDeleteWord = service.deleteWord(deleteWord);
        if(!canDeleteWord.isSuccess()){
            System.out.println(canDeleteWord.getMessage() + "\n最初からやり直してください。");
            return;
        }

        System.out.println(deleteWord + " を削除しました。");
    }
}
