package group.menu.validator;

public class GeneralValidator implements Validator {

    private final int minLengthInclusive;

    private final boolean csvInjectionDetection;
    private final InputType inputType;

    enum InputType {
        String, Number
    }

    public GeneralValidator(InputType inputType, int minLengthInclusive, boolean csvInjectionDetection) {
        this.inputType = inputType;
        this.minLengthInclusive = minLengthInclusive;
        this.csvInjectionDetection = csvInjectionDetection;
    }

    @Override
    public boolean validate(String input) {
        return input.length() >= minLengthInclusive && input.matches(".*[;,].*");
    }
}
