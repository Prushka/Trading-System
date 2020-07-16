
import group.repository.CSVRepository;
import group.repository.Repository;
import group.system.SaveHook;
import org.junit.Test;

public class ReflectionTest {

    @Test
    public void testCSVRepository() {
        SaveHook saveHook = new SaveHook();
        Repository<TestEntity> reflectionEntityRepository = new CSVRepository<>("data/test.csv", TestEntity::new, saveHook);
        reflectionEntityRepository.add(new TestEntity());
        saveHook.save();
    }
}
