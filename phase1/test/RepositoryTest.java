import group.item.Item;
import group.menu.processor.CSVInjectionPrevention;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.GeneralValidator;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.system.SaveHook;
import group.user.PersonalUser;
import group.user.PersonalUserManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepositoryTest {


    @Test
    public void personalUserManagerTest() {
        SaveHook saveHook = new SaveHook();
        Repository<PersonalUser> personalUserRepository = new CSVRepository<>("test/personal_user.csv", PersonalUser::new, saveHook);
        PersonalUserManager personalUserManager = new PersonalUserManager(personalUserRepository,
                new CSVRepository<>("test/item.csv", Item::new, saveHook));
        personalUserRepository.add(new PersonalUser("pika0","pika@m.m","12345678","25d55ad283aa40af464c76d713c7ad"));
        personalUserRepository.add(new PersonalUser("pika1","pika@m.m","12345678","25d55ad283aa40af464c76d713c7ad"));
        assertEquals(2, personalUserRepository.size());
        assertNull(personalUserRepository.getFirst(
                PersonalUser -> PersonalUser.getUserName().equals("pika")
                        && PersonalUser.getPassword().equals("25d55ad283aa40af464c76d713c7ad")));
        assertEquals("pika0", personalUserRepository.getFirst(
                PersonalUser -> PersonalUser.getUserName().equals("pika0")
                        && PersonalUser.getPassword().equals("25d55ad283aa40af464c76d713c7ad")).getUserName());
        assertEquals("pika1", personalUserRepository.getFirst(
                PersonalUser -> PersonalUser.getUserName().equals("pika1")
                        && PersonalUser.getPassword().equals("25d55ad283aa40af464c76d713c7ad")).getUserName());
    }
}
