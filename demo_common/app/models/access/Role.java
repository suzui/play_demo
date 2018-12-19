package models.access;

import models.organize.Organize;
import org.apache.commons.lang.StringUtils;
import vos.RoleVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role extends BaseRole {
    
    public static Role add(RoleVO vo) {
        Role root = new Role();
        root.organize = vo.organizeId != null ? Organize.findByID(vo.organizeId) : null;
        root.edit(vo);
        return root;
    }
    
    public void edit(RoleVO vo) {
        this.name = vo.name != null ? vo.name : name;
        this.accessIds = vo.accessIds != null ? StringUtils.join(vo.accessIds, ",") : accessIds;
        this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Role> fetch(RoleVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Role.find(defaultSql(StringUtils.join(hqls, " and ")) + vo.condition, params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(RoleVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Role.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(RoleVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.search)) {
            hqls.add("concat_ws(',',name) like ?");
            params.add("%" + vo.search + "%");
        }
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add(" name like ?");
            params.add("%" + vo.name + "%");
        }
        if (vo.organizeId != null) {
            hqls.add("organize.id=?");
            params.add(vo.organizeId);
        }
        return new Object[]{hqls, params};
    }
}
