package phase2.trade.validator;


/**
 * The interface Validator.
 *
 * @param <T> the type to be validated
 * @author Dan Lyu
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Validate boolean.
     *
     * @param input the input
     * @return the boolean
     */
    boolean validate(T input);

}
