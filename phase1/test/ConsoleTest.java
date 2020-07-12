import group.menu.processor.CSVInjectionPrevention;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {

    private final static Logger LOGGER = Logger.getLogger(ConsoleTest.class.getName());

    @Test
    public void injectionTest() {
        CSVInjectionPrevention csvInjectionPrevention = new CSVInjectionPrevention();
        String test1 = "a;2,3,,;";
        assertEquals(csvInjectionPrevention.process(test1), "a23");
    }


}
