package models.access;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class PermissionAccess extends BasePermissionAccess {
    
    public static PermissionAccess add(Permission permission, Access access) {
        PermissionAccess permissionAccess = findByPermissionAndAccess(permission, access);
        if (permissionAccess != null) {
            return permissionAccess;
        }
        permissionAccess = new PermissionAccess();
        permissionAccess.permission = permission;
        permissionAccess.access = access;
        return permissionAccess.save();
    }
    
    public Permission permission() {
        return (Permission) this.permission;
    }
    
    public Access access() {
        return (Access) this.access;
    }
    
    public static List<PermissionAccess> fetchByPermission(Permission permission) {
        return PermissionAccess.find(defaultSql("permission=?"), permission).fetch();
    }
    
    public static List<PermissionAccess> fetchByAccess(Access access) {
        return PermissionAccess.find(defaultSql("access=?"), access).fetch();
    }
    
}
