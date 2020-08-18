package phase2.trade.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * A validator that serves a general purpose.<p>
 * This validator checks if the input is a number / string, if the input is within a range of length and if the input containsUid potential csv injection characters.<p>
 * This validator can have other validators as children.
 *
 * @author Dan Lyu
 * @author CraigTP - <a href="https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java">regex for checking if a string is numberic</a>
 */
public class GeneralValidator implements Validator<String> {
    //This validator comes from phase 1 and is to be modified.

    /**
     * The minimum length allowed (inclusive) for the input String
     */
    private final int minLengthInclusive;

    /**
     * The maximum length allowed (inclusive) for the input String
     */
    private final int maxLengthInclusive;

    /**
     * <code>true</code> if injection characters are allowed
     */
    private final boolean csvInjectionDetection;

    /**
     * The input type to check
     */
    private final InputType inputType;

    /**
     * A list of validators as sub validators. This General Validator will also use this list of Validators to validate the input
     */
    private final List<Validator> validators = new ArrayList<>();

    /**
     * A list of regex used to validate the input
     */
    private final List<String> regexList = new ArrayList<>();

    /**
     * The intended input type the input String should be using
     */
    public enum InputType {
        String, Number
    }

    /**
     * @param inputType             The input type to check
     * @param minLengthInclusive    The minimum length allowed (inclusive) for the input String
     * @param maxLengthInclusive    The maximum length allowed (inclusive) for the input String
     * @param csvInjectionDetection <code>true</code> if injection characters are allowed
     */
    public GeneralValidator(InputType inputType, int minLengthInclusive, int maxLengthInclusive, boolean csvInjectionDetection) {
        this.inputType = inputType;
        this.minLengthInclusive = minLengthInclusive;
        this.maxLengthInclusive = maxLengthInclusive;
        this.csvInjectionDetection = csvInjectionDetection;
    }

    /**
     * @param validator the validator to be used together with this one
     * @return this GeneralValidator itself
     */
    public GeneralValidator addValidator(Validator validator) {
        validators.add(validator);
        return this;
    }

    /**
     * @param regex the regular expression to be used together with this GeneralValidator
     * @return this GeneralValidator itself
     */
    public GeneralValidator addRegex(String regex) {
        regexList.add(regex);
        return this;
    }

    /**
     * @param input String input
     * @return <code>true</code> if the input passes {@link #minLengthInclusive}, {@link #maxLengthInclusive}, {@link #regexList}, {@link #validators} and {@link #csvInjectionDetection}
     */
    @Override
    public boolean validate(String input) {
        boolean result = input.length() >= minLengthInclusive && input.length() <= maxLengthInclusive;
        if (csvInjectionDetection) {
            result = result && !input.matches(".*[;, ].*");
        }
        switch (inputType) {
            case String:
                break;
            case Number:
                result = result && input.matches("-?\\d+(\\.\\d+)?");
                break;
        }
        for (Validator validator : validators) {
            result = result && validator.validate(input);
        }
        for (String regex : regexList) {
            result = result && input.matches(regex);
        }
        return result;
    }
}
