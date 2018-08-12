package models.access;

import models.organize.Organize;
import org.apache.commons.lang.StringUtils;
import vos.RoleVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Role extends BaseRole {
    
    public static Role add(RoleVO roleVO) {
        Role permission = new Role();
        permission.organize = roleVO.organizeId != null ? Organize.findByID(roleVO.organizeId) : null;
        permission.edit(roleVO);
        return permission;
    }
    
    public void edit(RoleVO roleVO) {
        this.name = roleVO.name != null ? roleVO.name : name;
        this.accessIds = roleVO.accessIds != null ? StringUtils.join(roleVO.accessIds, ",") : accessIds;
        this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Role> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Role.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Role> fetchByOrganize(Organize organize) {
        return Role.find(defaultSql("organize = ?"), organize).fetch();
    }
    
    public static List<Role> fetchAll() {
        return Role.find(defaultSql()).fetch();
    }
    
    public static List<Role> fetch(RoleVO roleVO) {
        Object[] data = data(roleVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Role.find(defaultSql(StringUtils.join(hqls, " and ")) + roleVO.condition, params.toArray())
                .fetch(roleVO.page, roleVO.size);
    }
    
    public static int count(RoleVO roleVO) {
        Object[] data = data(roleVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Role.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(RoleVO roleVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(roleVO.search)) {
            hqls.add("concat_ws(',',name) like ?");
            params.add("%" + roleVO.search + "%");
        }
        if (roleVO.organizeId != null) {
            hqls.add("organize.id=?");
            params.add(roleVO.organizeId);
        }
        return new Object[]{hqls, params};
    }
}
