package group.menu.validator;

/**
 * The class used to validate if a String is a valid enum of certain class.
 *
 * @param <T> the enum class type
 * @author Dan Lyu
 */
public class EnumValidator<T extends Enum<T>> implements Validator {

    /**
     * The enum class to be checked
     */
    private final Class<T> clazz;

    public EnumValidator(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Returns <code>true</code> if the input String is a valid enum constant in the given class
     *
     * @param input String input
     * @return <code>true</code> if the input String is a valid enum constant in the given class
     */
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
