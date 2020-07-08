package group.menu.validator;

public class EnumValidator<T extends Enum<T>> implements Validator {

    private final Class<T> clazz;

    public EnumValidator(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean validate(String input) {
        try {
            Enum.valueOf(clazz, input);
            return true;
        } catch (IllegalArgumentException e) { //TODO: this should be unchecked
            return false;
        }
    }
}
