package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import models.access.Access;
import models.access.Permission;
import models.access.BasePermissionAccess;
import models.access.PermissionPerson;
import models.person.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionVO extends OneData {
    
    @DataField(name = "权限组id")
    public Long permissionId;
    @DataField(name = "权限组名称")
    public String name;
    @DataField(name = "权限列表")
    public List<AccessVO> accesses;
    @DataField(name = "成员列表")
    public List<PersonVO> persons;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "权限ids", demo = "[1,2,3]")
    public List<Long> accessIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "成员ids", demo = "[1,2,3]")
    public List<Long> personIds;
    
    public PermissionVO() {
    
    }
    
    public PermissionVO(Permission permission) {
        this.permissionId = permission.id;
        this.name = permission.name;
    }
    
    public PermissionVO permissionAccesses(List<BasePermissionAccess> permissionAccesses) {
        this.accesses = permissionAccesses.stream().map(pa -> new AccessVO(pa.access)).collect(Collectors.toList());
        return this;
    }
    
    public PermissionVO accesses(List<Access> accesses) {
        this.accesses = accesses.stream().map(a -> new AccessVO(a)).collect(Collectors.toList());
        return this;
    }
    
    public PermissionVO permissionPersons(List<PermissionPerson> permissionPersons) {
        this.persons = permissionPersons.stream().map(pp -> new PersonVO(pp.person)).collect(Collectors.toList());
        return this;
    }
    
    public PermissionVO persons(List<Person> persons) {
        this.persons = persons.stream().map(p -> new PersonVO(p)).collect(Collectors.toList());
        return this;
    }
    
}
