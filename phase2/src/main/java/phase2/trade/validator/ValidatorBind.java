package phase2.trade.validator;

import javafx.beans.property.StringProperty;

/**
 * The Validator bind.
 *
 * @author Dan Lyu
 */
public class ValidatorBind {


    private final StringProperty textToUpdate;

    private boolean allPass = true;

    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    /**
     * Constructs a new Validator bind.
     *
     * @param textToUpdate the text to update
     */
    public ValidatorBind(StringProperty textToUpdate) {
        this.textToUpdate = textToUpdate;
    }


    /**
     * Validate validator bind.
     *
     * @param type  the type
     * @param error the error
     * @param input the input
     * @return the validator bind
     */
    public ValidatorBind validate(ValidatorType type, String error, String input) {
        return this.validate(type, error, null, input);
    }

    /**
     * Validate validator bind.
     *
     * @param type  the type
     * @param error the error
     * @param pass  the pass
     * @param input the input
     * @return the validator bind
     */
    public ValidatorBind validate(ValidatorType type, String error, String pass, String input) {
        boolean result = validatorFactory.getStringValidator(type).validate(input);
        if (result) {
            if (pass != null) {
                textToUpdate.setValue(pass);
            }
        } else {
            textToUpdate.setValue(error);
        }
        allPass = allPass && result;
        return this;
    }

    /**
     * Is all pass boolean.
     *
     * @return the boolean
     */
    public boolean isAllPass() {
        return allPass;
    }
}
