import org.junit.Test;
import phase2.trade.config.yaml.ConfigManager;

public class ConfigTest {

    @Test
    public void testConfig() {
        ConfigManager configManager = new ConfigManager();
        configManager.save();
    }
}
