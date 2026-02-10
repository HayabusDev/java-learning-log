public enum ValidationRule {
    USER_ID_NOT_EMPTY(ValidationErrorCode.USER_ID_EMPTY) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (userId == null){
                return getCode();
            }

            if (userId.isBlank()){
                return getCode();
            }
            return null;
        }
    },
    USER_ID_LENGTH(ValidationErrorCode.USER_ID_LENGTH_OUT_OF_RANGE, 3, 20) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (userId.length() < getMin() || userId.length() > getMax()){
                return getCode();
            }
            return null;
        }
    },
    USER_ID_CHARS(ValidationErrorCode.USER_ID_INVALID_CHARS) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (!userId.matches(USER_ID_ALLOWED_PATTERN)){
                return getCode();
            }
            return null;
        }
    },

    PASSWORD_NOT_EMPTY(ValidationErrorCode.PASSWORD_EMPTY) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (password == null){
                return getCode();
            }

            if (password.isBlank()){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_TOO_SHORT(ValidationErrorCode.PASSWORD_TOO_SHORT, 8,64) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (password.length() < getMin()){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_TOO_LONG(ValidationErrorCode.PASSWORD_TOO_LONG, 8,64) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if (password.length() > getMax()){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_HAS_LETTER(ValidationErrorCode.PASSWORD_NO_LETTER) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if(!password.matches(PASSWORD_HAS_LETTER_PATTERN)){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_HAS_UPPER(ValidationErrorCode.PASSWORD_NO_UPPERCASE) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if(!password.matches(PASSWORD_HAS_UPPER_PATTERN)){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_HAS_NUMBER(ValidationErrorCode.PASSWORD_NO_NUMBER) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if(!password.matches(PASSWORD_HAS_NUMBER_PATTERN)){
                return getCode();
            }
            return null;
        }
    },
    PASSWORD_HAS_SYMBOL(ValidationErrorCode.PASSWORD_NO_SYMBOL) {
        @Override
        ValidationErrorCode check(String userId, String password) {
            if(!password.matches(PASSWORD_HAS_SYMBOL_PATTERN)){
                return getCode();
            }
            return null;
        }
    };

    private static final String USER_ID_ALLOWED_PATTERN = "^[a-zA-Z0-9]+$";
    private static final String PASSWORD_HAS_LETTER_PATTERN = ".*[a-zA-Z].*";
    private static final String PASSWORD_HAS_UPPER_PATTERN = ".*[A-Z].*";
    private static final String PASSWORD_HAS_NUMBER_PATTERN = ".*[0-9].*";
    private static final String PASSWORD_HAS_SYMBOL_PATTERN = ".*[!@#$%^&*_-].*";

    private final ValidationErrorCode code;
    private final int min;
    private final int max;

    //委譲コンストラクタ
    ValidationRule(ValidationErrorCode code){
        this(code, -1 ,-1);
    }

    ValidationRule(ValidationErrorCode code, int min, int max){
        this.code = code;
        this.min = min;
        this.max = max;
    }

    abstract ValidationErrorCode check(String userId, String password);

    protected ValidationErrorCode getCode() {
        return code;
    }

    protected int getMin() {
        return min;
    }

    protected int getMax() {
        return max;
    }
}
