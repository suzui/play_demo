package models.organize;

import models.person.Person;
import models.token.BaseOrganize;
import org.apache.commons.lang.StringUtils;
import vos.OrganizeVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Organize extends BaseOrganize {
    
    public static Organize init() {
        Organize organize = new Organize();
        organize.name = "机构";
        organize.rank = 0d;
        organize.save();
        organize.root = organize;
        return organize.save();
    }
    
    public static Organize add(OrganizeVO vo) {
        Organize organize = new Organize();
        organize.parent = Organize.findByID(vo.parentId);
        organize.rank = organize.parent().initRank();
        organize.root = Organize.findByID(getRoot());
        organize.edit(vo);
        return organize;
    }
    
    public void edit(OrganizeVO vo) {
        this.name = vo.name != null ? vo.name : name;
        this.logo = vo.logo != null ? vo.logo : logo;
        this.save();
    }
    
    public void del() {
        if (this.parent == null) {
            Person.fetchByOrganize(this).forEach(p -> p.del());
        }
        Relation.fetchByOrganize(this).forEach(r -> r.del());
        this.children().forEach(o -> o.del());
        this.logicDelete();
    }
    
    public static List<Organize> fetch(OrganizeVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Organize.find(defaultSql(StringUtils.join(hqls, " and ")) + vo.condition, params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(OrganizeVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Organize.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(OrganizeVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add("name like ?");
            params.add("%" + vo.name + "%");
        }
        if (vo.parentId != null) {
            hqls.add("parent.id=?");
            params.add(vo.parentId);
        }
        if (vo.rootId != null) {
            hqls.add("root.id=?");
            params.add(vo.rootId);
        }
        return new Object[]{hqls, params};
    }
}