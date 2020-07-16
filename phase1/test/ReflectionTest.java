
import group.menu.processor.CSVInjectionPrevention;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.GeneralValidator;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.system.SaveHook;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectionTest {

    @Test
    public void testCSVRepository() {
        SaveHook saveHook = new SaveHook();
        Repository<ReflectionEntity> reflectionEntityRepository = new CSVRepository<>("data/test.csv", ReflectionEntity::new, saveHook);
        reflectionEntityRepository.add(new ReflectionEntity());
        saveHook.save();
    }
}
