package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import models.access.Access;
import models.access.Authorization;
import models.access.Role;
import utils.BaseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RoleVO extends OneData {
    
    @DataField(name = "角色id")
    public Long roleId;
    @DataField(name = "角色名称")
    public String name;
    @DataField(name = "权限ids", demo = "[1,2,3]")
    public List<Long> accessIds;
    @DataField(name = "权限列表")
    public List<AccessVO> accesses;
    @DataField(name = "人员列表")
    public List<SimplePersonVO> persons;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "根机构id")
    public Long rootId;
    
    public RoleVO() {
        this.condition = " order by id";
    }
    
    public RoleVO(Role role) {
        super(role.id);
        this.roleId = role.id;
        this.name = role.name;
        if (role.root != null) {
            this.rootId = role.root.id;
        }
        this.accessIds = BaseUtils.idsToList(role.accessIds);
    }
    
    public RoleVO accesses(List<Access> accesses) {
        this.accesses = accesses.stream().map(a -> new AccessVO(a)).collect(Collectors.toList());
        return this;
    }
    
    public RoleVO persons(List<Authorization> authorizations) {
        this.persons = authorizations.stream().map(a -> new SimplePersonVO(a.person())).collect(Collectors.toList());
        return this;
    }
    
    
}
