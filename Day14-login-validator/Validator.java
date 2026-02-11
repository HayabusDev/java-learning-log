import java.util.ArrayList;
import java.util.List;

public class Validator {

    private final List<ValidationRule> userIdGate = List.of(ValidationRule.USER_ID_NOT_EMPTY);
    private final List<ValidationRule> userIdRules = List.of(ValidationRule.USER_ID_LENGTH, ValidationRule.USER_ID_CHARS);

    private final List<ValidationRule> passwordGate = List.of(ValidationRule.PASSWORD_NOT_EMPTY);
    private final List<ValidationRule> passwordRules = List.of(ValidationRule.PASSWORD_TOO_SHORT, ValidationRule.PASSWORD_TOO_LONG,
    ValidationRule.PASSWORD_HAS_LETTER, ValidationRule.PASSWORD_HAS_UPPER, ValidationRule.PASSWORD_HAS_NUMBER, ValidationRule.PASSWORD_HAS_SYMBOL);

    public Result<Void> validateUserId(String userId){
        return validateEngine(userId, null, userIdGate, userIdRules);
    }

    public Result<Void> validatePassword(String password){
        return validateEngine(null, password, passwordGate, passwordRules);
    }

    public Result<Void> validateRegistration(String userId, String password){
        List<ValidationRule> combinedGate = combineRulesList(userIdGate, passwordGate);
        List<ValidationRule> combinedFormat = combineRulesList(userIdRules, passwordRules);

        return validateEngine(userId, password ,combinedGate, combinedFormat);
    }

    public Result<Void> validateLogin(String userId, String password){
        List<ValidationRule> combinedGate = combineRulesList(userIdGate, passwordGate);
        List<ValidationRule> emptyFormat = new ArrayList<>();

        return validateEngine(userId, password, combinedGate, emptyFormat);
    }

    private List<ValidationRule> combineRulesList(List<ValidationRule> userIdRules, List<ValidationRule> passwordRules){
        List<ValidationRule> combinedList = new ArrayList<>();
        combinedList.addAll(userIdRules);
        combinedList.addAll(passwordRules);
        return combinedList;
    }

    private Result<Void> validateEngine(String userId, String password, List<ValidationRule> gateRules, List<ValidationRule> formatRules){
        List<ErrorCodeLike> requiredErrors = new ArrayList<>();
        for (ValidationRule validationRule : gateRules){
            ValidationErrorCode error = validationRule.check(userId, password);
            if (error != null){
                requiredErrors.add(error);
            }
        }
        Result<Void> checkedRequired = convertToValidationResult(requiredErrors);
        if (!checkedRequired.isSuccess()){
            return checkedRequired;
        }

        List<ErrorCodeLike> formatErrors = new ArrayList<>();
        for (ValidationRule validationRule : formatRules){
            ValidationErrorCode error = validationRule.check(userId, password);
            if (error != null){
                formatErrors.add(error);
            }
        }
        Result<Void> checkedFormat = convertToValidationResult(formatErrors);
        if(!checkedFormat.isSuccess()){
            return checkedFormat;
        }

        return Result.success();
    }

    private Result<Void> convertToValidationResult(List<ErrorCodeLike> errorCodeLikes){
        if (!errorCodeLikes.isEmpty()){
            return Result.failure(errorCodeLikes);
        }
        return Result.success();
    }
}
