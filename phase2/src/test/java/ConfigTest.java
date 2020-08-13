import org.junit.Test;
import phase2.trade.ShutdownHook;
import phase2.trade.config.ConfigBundle;

public class ConfigTest {

    @Test
    public void testConfig() {
        ShutdownHook shutdownHook = new ShutdownHook();
        ConfigBundle configBundle = new ConfigBundle();

        shutdownHook.addShutdownable(configBundle);
    }
}
