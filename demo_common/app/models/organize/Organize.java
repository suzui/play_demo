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
        organize.organize = organize;
        return organize.save();
    }
    
    public static Organize add(OrganizeVO organizeVO) {
        Organize organize = new Organize();
        organize.parent = Organize.findByID(organizeVO.parentId);
        organize.rank = organize.parent().initRank();
        organize.organize = Organize.findByID(getSource());
        organize.edit(organizeVO);
        return organize;
    }
    
    public void edit(OrganizeVO organizeVO) {
        this.name = organizeVO.name != null ? organizeVO.name : name;
        this.logo = organizeVO.logo != null ? organizeVO.logo : logo;
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
    
    public static List<Organize> fetch(OrganizeVO organizeVO) {
        Object[] data = data(organizeVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Organize.find(defaultSql(StringUtils.join(hqls, " and ")) + organizeVO.condition, params.toArray())
                .fetch(organizeVO.page, organizeVO.size);
    }
    
    public static int count(OrganizeVO organizeVO) {
        Object[] data = data(organizeVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Organize.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(OrganizeVO organizeVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(organizeVO.name)) {
            hqls.add("name like ?");
            params.add("%" + organizeVO.name + "%");
        }
        if (organizeVO.parentId != null) {
            hqls.add("parent.id=?");
            params.add(organizeVO.parentId);
        }
        hqls.add("organize.id=?");
        params.add(getSource());
        return new Object[]{hqls, params};
    }
}