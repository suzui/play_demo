package models.access;

import models.organize.Organize;
import models.person.Person;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class AccessPerson extends BaseAccessPerson {
    
    public static AccessPerson add(Access access, Person person) {
        AccessPerson accessPerson = findByAccessAndPerson(access, person);
        if (accessPerson != null) return accessPerson;
        accessPerson = new AccessPerson();
        accessPerson.access = access;
        accessPerson.person = person;
        return accessPerson.save();
    }
    
    public static AccessPerson add(Access access, Person person, Organize organize) {
        AccessPerson accessPerson = findByAccessAndPersonAndOrganize(access, person, organize);
        if (accessPerson != null) return accessPerson;
        accessPerson = new AccessPerson();
        accessPerson.access = access;
        accessPerson.person = person;
        accessPerson.organize = organize;
        return accessPerson.save();
    }
    
    public Access access() {
        return (Access) this.access;
    }
    
    public Person person() {
        return (Person) this.person;
    }
    
    public Organize organize() {
        return (Organize) this.organize;
    }
    
    public static List<AccessPerson> fetchByPerson(Person person) {
        return AccessPerson.find(defaultSql("person = ?"), person).fetch();
    }
    
    public static List<AccessPerson> fetchByPersonAndOrganize(Person person, Organize organize) {
        return AccessPerson.find(defaultSql("person = ? and organize=?"), person, organize).fetch();
    }
}
