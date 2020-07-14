package group.menu.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * A validator that serves a general purpose.<p>
 * This validator checks if the input is a number / string, if the input is within a range of length and if the input contains potential csv injection characters.<p>
 * This validator can have other validators as children.
 *
 * @author Dan Lyu
 * @author CraigTP - <a href="https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java">regex for checking if a string is numberic</a>
 */
public class GeneralValidator implements Validator {

    private final int minLengthInclusive;
    private final int maxLengthInclusive;

    private final boolean csvInjectionDetection;
    private final InputType inputType;

    private final List<Validator> validators = new ArrayList<>();

    public enum InputType {
        String, Number
    }

    public GeneralValidator(InputType inputType, int minLengthInclusive, int maxLengthInclusive, boolean csvInjectionDetection) {
        this.inputType = inputType;
        this.minLengthInclusive = minLengthInclusive;
        this.maxLengthInclusive = maxLengthInclusive;
        this.csvInjectionDetection = csvInjectionDetection;
    }

    public GeneralValidator addValidator(Validator validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public boolean validate(String input) {
        boolean result = input.length() >= minLengthInclusive && input.length() <= maxLengthInclusive;
        if (csvInjectionDetection) {
            result = result && !input.matches(".*[;,].*");
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
        return result;
    }
}
