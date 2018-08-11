package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import models.access.Access;
import models.access.Authorization;
import models.access.Permission;
import utils.BaseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionVO extends OneData {
    
    @DataField(name = "权限组id")
    public Long permissionId;
    @DataField(name = "权限组名称")
    public String name;
    @DataField(name = "机构id")
    public Long organizeId;
    @DataField(name = "权限ids", demo = "[1,2,3]")
    public List<Long> accessIds;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "权限列表")
    public List<AccessVO> accesses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "成员列表")
    public List<PersonVO> persons;
    
    public PermissionVO() {
        this.condition = " order by id";
    }
    
    public PermissionVO(Permission permission) {
        this.permissionId = permission.id;
        this.name = permission.name;
        if (permission.organize != null) {
            this.organizeId = permission.organize.id;
        }
        this.accessIds = BaseUtils.idsToList(permission.accessIds);
    }
    
    public PermissionVO accesses(List<Access> accesses) {
        this.accesses = accesses.stream().map(a -> new AccessVO(a)).collect(Collectors.toList());
        return this;
    }
    
    public PermissionVO persons(List<Authorization> authorizations) {
        this.persons = authorizations.stream().map(a -> new PersonVO(a.person())).collect(Collectors.toList());
        return this;
    }
    
    
}
