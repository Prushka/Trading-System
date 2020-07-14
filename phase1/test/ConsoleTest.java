import group.menu.processor.CSVInjectionPrevention;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsoleTest {

    @Test
    public void csvInjectionTest() {
        CSVInjectionPrevention csvInjectionPrevention = new CSVInjectionPrevention();
        String test1 = "a;2,3,,;";
        assertEquals(csvInjectionPrevention.process(test1), "a23");

        String test2 = "a2";

        assertTrue(test1.matches(".*[;,].*"));
        assertFalse(test2.matches(".*[;,].*"));
    }


}
