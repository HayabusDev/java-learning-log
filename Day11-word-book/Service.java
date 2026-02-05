import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Service {
    // 登録・検索・一覧・削除
    private final Repository repository;

    public Service(Repository repository){
        this.repository = repository;
    }

    private <T> Result<T> executeSafely(SafeAction<T> action, String operationName, String inputValue){
        try{
            return action.execute();

        }catch (IllegalArgumentException iae){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            iae.printStackTrace();

            return Result.failure(ErrorCode.INPUT_INVALID);

        }catch (IllegalStateException ise){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            ise.printStackTrace();

            return Result.failure(ErrorCode.SYSTEM_ERROR);

        }catch (RuntimeException re){

            System.err.println("[FATAL] " + operationName);
            System.err.println("input=" + inputValue);
            re.printStackTrace();

            return Result.failure(ErrorCode.SYSTEM_ERROR);

        }
    }

    public Result<Void> createWord(String addWord, String meaning){
        Result<Void> result = validateCreateWord(addWord, meaning);
        if (!result.isSuccess()){
            return result;
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                Words createdWords = new Words(addWord, meaning);
                repository.addToWordMap(addWord, createdWords);
                return Result.success();
            }
        };

        return executeSafely(action, "createWord", addWord);
    }

    public Result<Void> validateCreateWord(String wordKey, String meaning){
        if (!isValidWord(wordKey)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        if (!isValidMeaning(meaning)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        if (existsWord(wordKey)){
            return Result.failure(ErrorCode.DATA_DUPLICATE);
        }

        return Result.success();
    }

    public Result<Void> deleteWord(String wordKey){
        Result<Void> result = validateDeleteWord(wordKey);
        if (!result.isSuccess()){
            return result;
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                repository.removeWord(wordKey);
                return Result.success();
            }
        };

        return executeSafely(action, "deleteWord", wordKey);
    }

    public Result<Void> validateDeleteWord(String wordKey){
        if (!isValidWord(wordKey)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        if (!existsWord(wordKey)){
            return Result.failure(ErrorCode.DATA_NOT_FOUND);
        }

        return Result.success();
    }

    private boolean isValidWord(String wordKey){
        return wordKey != null && !wordKey.isBlank();
    }

    private boolean isValidMeaning(String meaning){
        return meaning != null && !meaning.isBlank();
    }

    private boolean existsWord(String addedWord){
        return repository.getWordByKey(addedWord) != null;
    }

    public Result<List<Words>> searchWord(String keyword){
        if (!isValidWord(keyword)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        SafeAction<List<Words>> action = new SafeAction<List<Words>>() {
            @Override
            public Result<List<Words>> execute() {
                List<Words> matchedList = new ArrayList<>();
                List<Words> convertedList = convertMapToList();

                for (Words word : convertedList){
                    if(word.getWord().contains(keyword)){
                        matchedList.add(word);
                    }
                }

                return Result.success(matchedList);
            }
        };

        return executeSafely(action, "searchWord", keyword);
    }

    //a-z順にソートしてから一覧表示
    //将来拡張のため、sortWordメソッドを作成
    private List<Words> sortWord(List<Words> wordsList){
        if (wordsList == null){
            throw new IllegalArgumentException("wordsList is null");
        }
        //引数リストをコピーしてからソートする 「なんで順番変わった!?」系バグをほぼ回避
        List<Words> copiedList = new ArrayList<>(wordsList);
        copiedList.sort(Comparator.comparing(Words::getWord));
        return  copiedList;
    }

    public Result<List<Words>> listWord(){
        SafeAction<List<Words>> action = new SafeAction<List<Words>>() {
            @Override
            public Result<List<Words>> execute() {
                return Result.success(sortWord(convertMapToList()));
            }
        };
        return executeSafely(action, "listWord", "no-input");
    }

    private List<Words> convertMapToList(){
        return new ArrayList<>(repository.findAllWords().values());
    }
}
