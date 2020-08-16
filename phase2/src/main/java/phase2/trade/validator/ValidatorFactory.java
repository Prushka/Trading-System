package phase2.trade.validator;

public class ValidatorFactory {

    public ValidatorFactory() {

    }

    public Validator getValidator(ValidatorType type) {
        switch (type) {
            case TELEPHONE:
                return new GeneralValidator(GeneralValidator.InputType.Number, 5, 12, true);
            case PASSWORD:
                return new GeneralValidator(GeneralValidator.InputType.String, 8, 30, true);
            case USER_NAME:
                return new GeneralValidator(GeneralValidator.InputType.String, 4, 15, true);
            case EMAIL:
                return new EmailValidator();
            case NOT_EMPTY:
                return input -> input != null && !input.isEmpty();
        }
        return null;
    }
}
