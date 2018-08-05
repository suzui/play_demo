package models.access;

import models.organize.Organize;
import models.person.Person;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class PermissionPerson extends BasePermissionPerson {
    
    public static PermissionPerson add(Permission permission, Person person) {
        PermissionPerson permissionPerson = findByPermissionAndPerson(permission, person);
        if (permissionPerson != null) {
            return permissionPerson;
        }
        permissionPerson = new PermissionPerson();
        permissionPerson.permission = permission;
        permissionPerson.person = person;
        return permissionPerson.save();
    }
    
    public Permission permission() {
        return (Permission) this.permission;
    }
    
    public Person person() {
        return (Person) this.person;
    }
    
    public static List<PermissionPerson> fetchByPermission(Permission permission) {
        return PermissionPerson.find(defaultSql("permission=?"), permission).fetch();
    }
    
    public static List<PermissionPerson> fetchByPerson(Person person) {
        return PermissionPerson.find(defaultSql("person=?"), person).fetch();
    }
    
    public static List<PermissionPerson> fetchByPersonAndOrganize(Person person, Organize organize) {
        return PermissionPerson.find(defaultSql("person=? and permission.organize=?"), person, organize).fetch();
    }
    
}
