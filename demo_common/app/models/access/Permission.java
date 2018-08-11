package models.access;

import models.organize.Organize;
import org.apache.commons.lang.StringUtils;
import vos.PermissionVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Permission extends BasePermission {
    
    public static Permission add(PermissionVO permissionVO) {
        Permission permission = new Permission();
        permission.organize = permissionVO.organizeId != null ? Organize.findByID(permissionVO.organizeId) : null;
        permission.edit(permissionVO);
        return permission;
    }
    
    public void edit(PermissionVO permissionVO) {
        this.name = permissionVO.name != null ? permissionVO.name : name;
        this.accessIds = permissionVO.accessIds != null ? StringUtils.join(permissionVO.accessIds, ",") : accessIds;
        this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Permission> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Permission.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Permission> fetchByOrganize(Organize organize) {
        return Permission.find(defaultSql("organize = ?"), organize).fetch();
    }
    
    public static List<Permission> fetchAll() {
        return Permission.find(defaultSql()).fetch();
    }
    
    public static List<Permission> fetch(PermissionVO permissionVO) {
        Object[] data = data(permissionVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Permission.find(defaultSql(StringUtils.join(hqls, " and ")) + permissionVO.condition, params.toArray())
                .fetch(permissionVO.page, permissionVO.size);
    }
    
    public static int count(PermissionVO permissionVO) {
        Object[] data = data(permissionVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Permission.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PermissionVO permissionVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(permissionVO.search)) {
            hqls.add("concat_ws(',',name) like ?");
            params.add("%" + permissionVO.search + "%");
        }
        if (permissionVO.organizeId != null) {
            hqls.add("organize.id=?");
            params.add(permissionVO.organizeId);
        }
        return new Object[]{hqls, params};
    }
}
