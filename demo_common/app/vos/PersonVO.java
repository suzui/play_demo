package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import enums.PersonType;
import enums.Sex;
import models.access.Access;
import models.access.Role;
import models.organize.Organize;
import models.person.Person;
import models.token.AccessToken;
import utils.BaseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PersonVO extends OneData {
    
    @DataField(name = "用户id")
    public Long personId;
    @DataField(name = "用户名")
    public String username;
    @DataField(name = "手机号")
    public String phone;
    @DataField(name = "邮箱")
    public String email;
    @DataField(name = "编号")
    public String number;
    @DataField(name = "姓名")
    public String name;
    @DataField(name = "头像")
    public String avatar;
    @DataField(name = "简介")
    public String intro;
    @DataField(name = "备注")
    public String remark;
    @DataField(name = "性别", enums = Sex.class)
    public Integer sex;
    @DataField(name = "类别", enums = PersonType.class)
    public Integer type;
    @DataField(name = "token")
    public String accesstoken;
    @JsonInclude(Include.NON_NULL)
    @DataField(name = "密码")
    public String password;
    @DataField(name = "首次登录时间")
    public Long firstLoginTime;
    @DataField(name = "最后登录时间")
    public Long lastLoginTime;
    @DataField(name = "登录次数")
    public Integer loginAmount;
    
    @DataField(name = "权限code列表")
    public List<String> accessCodes;
    @DataField(name = "权限列表")
    public List<AccessVO> access;
    @DataField(name = "角色ids")
    public List<Long> roleIds;
    @DataField(name = "角色列表")
    public List<RoleVO> roles;
    @DataField(name = "授权列表")
    public List<AuthorizationVO> authorizations;
    
    public PersonVO() {
    }
    
    public PersonVO(Person person) {
        this.personId = person.id;
        this.username = person.username;
        this.phone = person.phone;
        this.email = person.email;
        this.number = person.number;
        this.name = person.name;
        this.avatar = person.avatar;
        this.intro = person.intro;
        this.remark = person.remark;
        this.sex = person.sex.code();
        this.type = person.type.code();
        this.firstLoginTime = person.firstLoginTime;
        this.lastLoginTime = person.lastLoginTime;
        this.loginAmount = person.loginAmount;
    }
    
    public PersonVO(AccessToken accessToken) {
        this(accessToken.person());
        Person person = accessToken.person();
        this.accesstoken = accessToken.accesstoken;
        List<Access> access = person.isAdmin() ? person.access() : person.access(Organize.findByID(BaseUtils.getRoot()));
        List<Role> roles = person.isAdmin() ? person.roles() : person.roles(Organize.findByID(BaseUtils.getRoot()));
        this.accessCodes = access.stream().map(a -> a.code).collect(Collectors.toList());
        this.access = access.stream().map(a -> new AccessVO(a)).collect(Collectors.toList());
        this.roleIds = roles.stream().map(r -> r.id).collect(Collectors.toList());
        this.roles = roles.stream().map(r -> new RoleVO(r)).collect(Collectors.toList());
    }
    
    public PersonVO roles(List<Role> roles) {
        this.roleIds = roles.stream().map(r -> r.id).collect(Collectors.toList());
        this.roles = roles.stream().map(r -> new RoleVO(r)).collect(Collectors.toList());
        return this;
    }
    
}
