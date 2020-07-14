package group.menu.processor;

/**
 * The pre-processor used to remove potential injection characters from user input.
 *
 * @author Dan Lyu
 */
public class CSVInjectionPrevention implements InputPreProcessor {
    /**
     * @param input the raw input String
     * @return the process String without injection characters
     */
    @Override
    public String process(String input) {
        return input.replaceAll("[;,]", "");
    }
}
