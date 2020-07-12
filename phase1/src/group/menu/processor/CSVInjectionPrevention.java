package group.menu.processor;

public class CSVInjectionPrevention implements InputPreProcessor {
    @Override
    public String process(String input) {
        return input.replaceAll("[;,]", "");
    }
}
