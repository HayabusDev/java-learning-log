package tikectSalesSimulator.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Result<T> {
    private final boolean success;
    private final List<ErrorCodeLike> errorCodeLikes;
    private final String message;
    private final T data;


    private Result(boolean success, List<ErrorCodeLike> errorCodeLikes, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;

        List<ErrorCodeLike> baseList;
        baseList = Objects.requireNonNullElseGet(errorCodeLikes, ArrayList::new);
        List<ErrorCodeLike> copiedErrorCodeLikes = new ArrayList<>(baseList);
        this.errorCodeLikes = Collections.unmodifiableList(copiedErrorCodeLikes);
    }

    //成功(データあり)
    public static <T> Result<T> success(T data){
        return new Result<>(true, Collections.emptyList(), null, data);
    }

    //成功（データなし）
    public static <T> Result<T> success(){
        return new Result<>(true, Collections.emptyList(), null, null);
    }

    //失敗（単発エラー）
    public static <T> Result<T> failure(ErrorCodeLike errorCodeLike){
        if (errorCodeLike == null){
            throw new IllegalArgumentException("errorCodeLike must not be null");
        }

        List<ErrorCodeLike> oneErrorCodeList = new ArrayList<>();
        oneErrorCodeList.add(errorCodeLike);

        return failure(oneErrorCodeList);
    }

    //失敗（複数エラー）
    public static <T> Result<T> failure(List<ErrorCodeLike> errorCodeLikes){
        if (errorCodeLikes == null || errorCodeLikes.isEmpty()){
            throw new IllegalArgumentException("errorCodeLikes must not be null or empty");
        }

        return new Result<>(false, errorCodeLikes, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ErrorCodeLike> getErrorCodeLikes() {
        return errorCodeLikes;
    }

    //将来用、このResultを汎用に使いたいため残しておく
    public String getMessage() {
        return message;
    }

    public T getData(){
        return data;
    }
}
