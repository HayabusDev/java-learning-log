package tikectSalesSimulator.result;

public enum SystemErrorCode implements ErrorCodeLike {
    INPUT_INVALID("INPUT_001", "入力が不正です。"),

    DATA_NOT_FOUND("DATA_001", "登録されていません。"),
    DATA_DUPLICATE("DATA_002", "既に登録されています。"),

    LOGIN_FAILED("AUTH_001", "IDまたはパスワードが正しくありません。"),
    OPERATION_NOT_ALLOWED("AUTH_002", "この処理は実行可能な条件を満たしていません。"),

    SYSTEM_ERROR("SYS_001", "システムエラーが発生しました。"),

    UNKNOWN_ERROR("UNKNOWN", "不明なエラーが発生しました。");

    private final String code;
    private final String message;

    SystemErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode(){
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }

    //codeからEnumを取得
    public static SystemErrorCode fromCode(String code){
        if (code == null){
            return UNKNOWN_ERROR;
        }

        for (SystemErrorCode error : values()){
            if (error.code.equals(code)){
                return error;
            }
        }

        //見つからなかった場合は不明なエラー
        return UNKNOWN_ERROR;
    }
}
