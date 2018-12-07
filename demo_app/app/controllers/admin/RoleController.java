package controllers.admin;

import annotations.ActionMethod;
import enums.AccessType;
import models.access.Role;
import models.person.Person;
import vos.AccessVO;
import vos.PageData;
import vos.Result;
import vos.RoleVO;

import java.util.List;
import java.util.stream.Collectors;

public class RoleController extends ApiController {
    
    @ActionMethod(name = "角色列表", param = "page,size", clazz = {PageData.class, RoleVO.class})
    public static void list(RoleVO vo) {
        int total = Role.count(vo);
        List<Role> roles = Role.fetch(vo);
        List<RoleVO> roleVOS = roles.stream().map(o -> new RoleVO(o)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, roleVOS)));
    }
    
    @ActionMethod(name = "角色详情", param = "roleId", clazz = RoleVO.class)
    public static void info(RoleVO vo) {
        Role role = Role.findByID(vo.roleId);
        RoleVO roleVO = new RoleVO(role);
        roleVO.accesses(role.access());
        renderJSON(Result.succeed(roleVO));
    }
    
    @ActionMethod(name = "角色新增", param = "name,accessIds", clazz = RoleVO.class)
    public static void add(RoleVO vo) {
        Role role = Role.add(vo);
        renderJSON(Result.succeed(new RoleVO(role)));
    }
    
    @ActionMethod(name = "角色编辑", param = "roleId,-name,-accessIds")
    public static void edit(RoleVO vo) {
        Role role = Role.findByID(vo.roleId);
        role.edit(vo);
        renderJSON(Result.succeed(new RoleVO(role)));
    }
    
    @ActionMethod(name = "角色删除", param = "roleId")
    public static void delete(RoleVO vo) {
        Role role = Role.findByID(vo.roleId);
        role.del();
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "权限列表", clazz = {PageData.class, AccessVO.class})
    public static void accessList() {
        renderJSON(Result.succeed(new PageData(AccessVO.list(AccessType.BOS))));
    }
    
}
