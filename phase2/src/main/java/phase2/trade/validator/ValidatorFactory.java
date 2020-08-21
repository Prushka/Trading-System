package phase2.trade.validator;

/**
 * The Validator factory.
 *
 * @author Dan Lyu
 */
public class ValidatorFactory {

    /**
     * Constructs a new Validator factory.
     */
    public ValidatorFactory() {

    }

    /**
     * Gets string validator.
     *
     * @param type the type
     * @return the string validator
     */
    public Validator<String> getStringValidator(ValidatorType type) {
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
