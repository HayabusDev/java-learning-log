import java.util.ArrayList;
import java.util.List;

public class Service {

    private final Repository repository;

    public Service(Repository repository){
        this.repository = repository;
    }

    private Result<Void> executeSafely(SafeAction action, String operationName, String userId){
        try{
            return action.execute();

        }catch (IllegalArgumentException iae){

            System.err.println("[ERROR] " + operationName);
            System.err.println("userId=" + userId);
            iae.printStackTrace();

            return Result.failure(ErrorCode.INPUT_INVALID);

        }catch (IllegalStateException ise){

            System.err.println("[ERROR] " + operationName);
            System.err.println("userId=" + userId);
            ise.printStackTrace();

            return Result.failure(ErrorCode.SYSTEM_ERROR);

        }catch (RuntimeException re){

            System.err.println("[FATAL] " + operationName);
            System.err.println("userId=" + userId);
            re.printStackTrace();

            return Result.failure(ErrorCode.SYSTEM_ERROR);

        }
    }

    //validateAddUserを通してからaddUserする
    public Result<Void> addUser(String userId){
        Result<Void> result = validateAddUser(userId);
        if (!result.isSuccess()){
            return result;
        }

        //メソッドを変数actionにして、インターフェースSafeActionで包んで、executeSafely()に渡す
        //処理を関数オブジェクトにして、安全実行メソッドに委譲する
        SafeAction action = new SafeAction() {
            @Override
            public Result<Void> execute() {
                User createdUser = new User(userId);
                repository.addToUserMap(userId, createdUser);
                return Result.success();
            }
        };

        return executeSafely(action, "addUser", userId);
    }

    public Result<Void> validateAddUser(String userId){
        if (!isValidUser(userId)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        if (existsUser(userId)){
            return Result.failure(ErrorCode.DATA_DUPLICATE);
        }
        return Result.success();
    }

    private boolean isValidUser(String userId){
        return userId != null && !userId.isBlank();
    }

    public boolean existsUser(String userId){
        return repository.getUserByKey(userId) != null;
    }

    public Result<List<User>> searchUser(String keyword){
        if (!isValidUser(keyword)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        List<User> matchedList = new ArrayList<>();
        List<User> convertedList = convertMapToList();

        for (User user : convertedList){
            if(user.getUserId().contains(keyword)){
                matchedList.add(user);
            }
        }

        // 0件でも成功 0=見つかりません
        return Result.success(matchedList);
    }

    private List<User> convertMapToList(){
        return new ArrayList<>(repository.findAllUser().values());
    }

    public Result<Void> deleteUser(String userId){
        Result<Void> result = validateDeleteUser(userId);
        if (!result.isSuccess()){
            return result;
        }

        SafeAction action = new SafeAction() {
            @Override
            public Result<Void> execute() {
                if (!repository.removeUserMap(userId)){
                    return Result.failure(ErrorCode.DATA_NOT_FOUND);
                }
                return Result.success();
            }
        };

        return executeSafely(action, "deleteUser", userId);
    }

    public Result<Void> validateDeleteUser(String userId){
        if (!isValidUser(userId)){
            return Result.failure(ErrorCode.INPUT_INVALID);
        }

        if (!existsUser(userId)){
            return Result.failure(ErrorCode.DATA_NOT_FOUND);
        }

        return Result.success();
    }
}
