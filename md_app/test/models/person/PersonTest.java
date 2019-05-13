package models.person;

import enums.PersonType;
import models.BaseModelTest;
import models.TestDataSource;
import org.junit.Test;

import java.util.List;

@TestDataSource(source = {"person"})
public class PersonTest extends BaseModelTest {
    
    @Override
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFind() {
        Person person = Person.findByPhone("123456789", PersonType.ADMIN);
        assertNotNull(person);
    }
    
    @Test
    public void testFetch() {
        List<Person> personList = Person.fetchAll();
        assertTrue(personList.size() > 0);
    }
    
}
