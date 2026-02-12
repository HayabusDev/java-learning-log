public enum SystemErrorCode {
    INPUT_INVALID("INPUT_001", "入力が不正です。"),

    DATA_NOT_FOUND("DATA_001", "登録されていません。"),
    DATA_DUPLICATE("DATA_002", "既に登録されています。"),
    RESERVATION_CONFLICT("DATA_003", "枠が埋まっています。予約できません。"),

    LOGIN_FAILED("AUTH_001", "IDまたはパスワードが正しくありません。"),

    SYSTEM_ERROR("SYS_001", "システムエラーが発生しました。"),

    UNKNOWN_ERROR("UNKNOWN", "不明なエラーが発生しました。");

    private final String code;
    private final String message;

    SystemErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return code;
    }

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
