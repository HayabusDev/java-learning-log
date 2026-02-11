import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Service {

    //TODO 登録・ログイン・一覧

    private final Repository repository;
    private final Validator validator;

    public Service(Repository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    private <T> Result<T> executeSafely(SafeAction<T> action, String operationName, String inputValue){
        try{
            return action.execute();

        }catch (IllegalArgumentException iae){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            iae.printStackTrace();

            return Result.failure(SystemErrorCode.INPUT_INVALID);

        }catch (IllegalStateException ise){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            ise.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }catch (RuntimeException re){

            System.err.println("[FATAL] " + operationName);
            System.err.println("input=" + inputValue);
            re.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }
    }

    public Result<Void> register(String userId, String password){
        Result<Void> canRegister = validator.validateRegistration(userId, password);
        if (!canRegister.isSuccess()){
            return canRegister;
        }

        if (existsUserId(userId)){
            return Result.failure(SystemErrorCode.DATA_DUPLICATE);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                User registeredUser = new User(userId, password);
                repository.addUserToMap(userId, registeredUser);
                return Result.success();
            }
        };

        return executeSafely(action, "register", userId);
    }

    public Result<Void> login(String userId, String password){
        Result<Void> canLogin = validator.validateLogin(userId, password);
        if (!canLogin.isSuccess()){
            return canLogin;
        }

        User userInfo = repository.getUser(userId);
        if (userInfo == null){
            return Result.failure(SystemErrorCode.LOGIN_FAILED);
        }

        if(!Objects.equals(userInfo.getPassword(), password)){
            return Result.failure(SystemErrorCode.LOGIN_FAILED);
        }

        //SafeActionはRepository に何か書き込む例外が起きる場合に使用する
        //ログイン試行回数を更新する、最終ログイン時刻を保存する　など
        return Result.success();
    }

    private boolean existsUserId(String userId){
        return repository.getUser(userId) != null;
    }

    public Result<List<User>> listUser(){
        SafeAction<List<User>> action = new SafeAction<List<User>>() {
            @Override
            public Result<List<User>> execute() {
                return Result.success(convertMapToList());
            }
        };

        return executeSafely(action, "listUser", "no-input");
    }

    private List<User> convertMapToList(){
        return new ArrayList<>(repository.findAllUsers().values());
    }
}
