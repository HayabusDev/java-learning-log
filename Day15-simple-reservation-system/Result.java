public class Result<T> {
    private final boolean success;
    private final SystemErrorCode errorCode;
    private final String message;
    private final T data;


    private Result(boolean success, SystemErrorCode errorCode, String message, T data){
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    //成功(データあり)
    public static <T> Result<T> success(T data){
        return new Result<>(true, null, null, data);
    }

    //成功（データなし）
    public static <T> Result<T> success(){
        return new Result<>(true, null, null, null);
    }

    public static <T> Result<T> failure(SystemErrorCode errorCode){
        if (errorCode == null){
            throw new NullPointerException(SystemErrorCode.UNKNOWN_ERROR.getMessage());
        }

        return new Result<>(false, errorCode, errorCode.getMessage(), null);
    }

    public boolean isSuccess() {
        return success;
    }

    public SystemErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData(){
        return data;
    }
}
