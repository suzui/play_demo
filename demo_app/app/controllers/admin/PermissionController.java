package controllers.admin;

import annotations.ActionMethod;
import models.access.Permission;
import models.access.PermissionAccess;
import models.access.PermissionPerson;
import models.person.Person;
import vos.AccessVO;
import vos.PageData;
import vos.PermissionVO;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionController extends ApiController {
    
    @ActionMethod(name = "权限组列表", param = "page,size", clazz = {PageData.class, PermissionVO.class})
    public static void permissionList(PermissionVO vo) {
        Person admin = getPersonByToken();
        int total = Permission.count(vo);
        List<Permission> permissions = Permission.fetch(vo);
        List<PermissionVO> permissionVOs = permissions.stream().map(o -> new PermissionVO(o)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, permissionVOs)));
    }
    
    @ActionMethod(name = "权限组详情", param = "permissionId", clazz = PermissionVO.class)
    public static void permissionInfo(PermissionVO vo) {
        Person admin = getPersonByToken();
        Permission permission = Permission.findByID(vo.permissionId);
        PermissionVO permissionVO = new PermissionVO(permission);
        permissionVO.permissionAccesses(PermissionAccess.fetchByPermission(permission));
        permissionVO.permissionPersons(PermissionPerson.fetchByPermission(permission));
        renderJSON(Result.succeed(permissionVO));
    }
    
    @ActionMethod(name = "权限组新增", param = "name,accessIds,personIds", clazz = PermissionVO.class)
    public static void permissionAdd(PermissionVO vo) {
        Person admin = getPersonByToken();
        Permission permission = Permission.add(vo);
        renderJSON(Result.succeed(new PermissionVO(permission)));
    }
    
    @ActionMethod(name = "权限组编辑", param = "permissionId,-name,-accessIds,-personIds")
    public static void permissionEdit(PermissionVO vo) {
        Person admin = getPersonByToken();
        Permission permission = Permission.findByID(vo.permissionId);
        permission.edit(vo);
        renderJSON(Result.succeed(new PermissionVO(permission)));
    }
    
    @ActionMethod(name = "权限组删除", param = "permissionId")
    public static void permissionDelete(PermissionVO vo) {
        Person admin = getPersonByToken();
        Permission permission = Permission.findByID(vo.permissionId);
        permission.del();
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "权限列表", clazz = {PageData.class, AccessVO.class})
    public static void accessList() {
        Person admin = getPersonByToken();
        renderJSON(Result.succeed(new PageData(AccessVO.list())));
    }
    
}
