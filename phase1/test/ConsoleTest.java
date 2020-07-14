import group.menu.processor.CSVInjectionPrevention;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.GeneralValidator;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsoleTest {

    @Test
    public void csvInjectionTest() {
        CSVInjectionPrevention csvInjectionPrevention = new CSVInjectionPrevention();
        String test1 = "a;2,3,,;";
        assertEquals(csvInjectionPrevention.process(test1), "a23");

        String test2 = "a2";
    }

    @Test
    public void generalValidatorTest() {
        GeneralValidator generalValidator = new GeneralValidator(GeneralValidator.InputType.Number, 5, 10, true);
        assertFalse(generalValidator.validate("1232344;,"));
        assertTrue(generalValidator.validate("1232344"));
        assertTrue(generalValidator.validate("1232344.2"));
        assertFalse(generalValidator.validate("123="));
        assertFalse(generalValidator.validate("123"));

        GeneralValidator generalValidator2 = new GeneralValidator(GeneralValidator.InputType.String, 3, 10, true);
        assertTrue(generalValidator2.validate("mmm"));
    }

    @Test
    public void PasswordEncryptionTest() {
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String password = "pikapika";
        String encrypted = passwordEncryption.process(password);
        assertEquals(passwordEncryption.process("pikapika"), encrypted);
        assertNotEquals(passwordEncryption.process("pikapika2"), encrypted);
    }


}
