package phase2.trade.validator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ValidatorBind {


    private StringProperty textToUpdate;

    private boolean allPass = true;

    private ValidatorFactory validatorFactory = new ValidatorFactory();

    public ValidatorBind(StringProperty textToUpdate) {
        this.textToUpdate = textToUpdate;
    }


    public ValidatorBind validate(ValidatorType type, String error, String input) {
        return this.validate(type, error, null, input);
    }

    public ValidatorBind validate(ValidatorType type, String error, String pass, String input) {
        boolean result = validatorFactory.getValidator(type).validate(input);
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

    public boolean isAllPass() {
        return allPass;
    }
}
