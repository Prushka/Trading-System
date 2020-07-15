package group.menu.validator;

import group.menu.data.Response;
import group.menu.node.ResponseNode;

/**
 * The pair of validator and ResponseNode.<p>
 * If user input passes the {@link #validator}, the {@link #failResponseNode} won't be used.<p>
 * If it didn't, the {@link #failResponseNode} will be the place where failed result is stored.<p>
 *
 * @author Dan Lyu
 * @see ResponseNode
 */
public class ValidatorPair {

    /**
     * The validator used to validate user input
     */
    private final Validator validator;

    /**
     * The response to be used if the validation didn't pass
     */
    private final Response failResponse;

    /**
     * @param validator    The validator used to validate user input
     * @param failResponse The response to be used if the validation didn't pass
     */
    public ValidatorPair(Validator validator, Response failResponse) {
        this.validator = validator;
        this.failResponse = failResponse;
    }

    /**
     * @param value user input
     * @return <code>true</code> if user input passes the validation
     */
    public boolean validate(String value) {
        if (validator == null) return true;
        return validator.validate(value);
    }

    /**
     * @return the response to be used if the validation didn't pass
     */
    public Response getFailResponse() {
        return failResponse;
    }
}
