package group.menu.validator;

import java.util.ArrayList;
import java.util.List;

public class GeneralValidator implements Validator {

    private final int minLengthInclusive;

    private final boolean csvInjectionDetection;
    private final InputType inputType;

    private final List<Validator> validators = new ArrayList<>();

    public enum InputType {
        String, Number
    }

    public GeneralValidator(InputType inputType, int minLengthInclusive, boolean csvInjectionDetection) {
        this.inputType = inputType;
        this.minLengthInclusive = minLengthInclusive;
        this.csvInjectionDetection = csvInjectionDetection;
    }

    public GeneralValidator addValidator(Validator validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public boolean validate(String input) {
        boolean result = input.length() >= minLengthInclusive;
        if (csvInjectionDetection) {
            result = result && input.matches(".*[;,].*");
        }
        switch (inputType) {
            case String:
                break;
            case Number:
                // TODO: what's the regex
                break;
        }
        for (Validator validator : validators) {
            result = result && validator.validate(input);
        }
        return result;
    }
}
