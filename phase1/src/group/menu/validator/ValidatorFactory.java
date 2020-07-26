package group.menu.validator;

public class ValidatorFactory {

    public ValidatorFactory(){

    }

    public enum Type{
        TELEPHONE, PASSWORD, USER_NAME
    }

    public Validator getValidator(Type type){
        switch (type){
            case TELEPHONE:
                return new GeneralValidator(GeneralValidator.InputType.Number, 5, 12, true);
            case PASSWORD:
                return new GeneralValidator(GeneralValidator.InputType.String, 8, 30, true);
            case USER_NAME:
                return new GeneralValidator(GeneralValidator.InputType.String, 4, 15, true);
        }
        return null;
    }
}
