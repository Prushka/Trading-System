import group.menu.processor.CSVInjectionPrevention;
import group.menu.validator.GeneralValidator;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ConsoleTest {

    private final static Logger LOGGER = Logger.getLogger(ConsoleTest.class.getName());

    @Test
    public void injectionTest() {
        CSVInjectionPrevention csvInjectionPrevention = new CSVInjectionPrevention();
        String test1 = "a;2,3,,;";
        assertEquals(csvInjectionPrevention.process(test1), "a23");

        String test2 = "a2";

        assertTrue(test1.matches(".*[;,].*"));
        assertFalse(test2.matches(".*[;,].*"));

        GeneralValidator generalValidator = new GeneralValidator(GeneralValidator.InputType.Number, 5, true);
        assertTrue(generalValidator.validate("123.234"));
    }


}
