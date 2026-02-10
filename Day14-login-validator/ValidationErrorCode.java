public enum ValidationErrorCode implements ErrorCodeLike {
    USER_ID_EMPTY("ID_001", "IDが空です。"),
    USER_ID_LENGTH_OUT_OF_RANGE("ID_002", "IDが短すぎるか、長すぎます。" ),
    USER_ID_INVALID_CHARS("ID_003", "IDに記号は使用できません。"),

    PASSWORD_EMPTY("PASS_000", "パスワードが空です。"),
    PASSWORD_TOO_SHORT("PASS_001", "パスワードが短すぎます。8文字以上入力してください。"),
    PASSWORD_TOO_LONG("PASS_002", "パスワードが長すぎます。"),
    PASSWORD_NO_LETTER("PASS_003", "1つ以上文字が必要です。"),
    PASSWORD_NO_UPPERCASE("PASS_004", "大文字が1つ以上必要です。"),
    PASSWORD_NO_NUMBER("PASS_005", "数字が1つ以上必要です。"),
    PASSWORD_NO_SYMBOL("PASS_006", "記号が1つ以上必要です。"),
    UNKNOWN_ERROR("UNKNOWN", "不明なエラーが発生しました。");

    private final String code;
    private final String message;

    ValidationErrorCode(String code, String message){
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
    public static ValidationErrorCode fromCode(String code){
        if (code == null){
            return UNKNOWN_ERROR;
        }

        for (ValidationErrorCode error : values()){
            if (error.code.equals(code)){
                return error;
            }
        }

        //見つからなかった場合は不明なエラー
        return UNKNOWN_ERROR;
    }
}
