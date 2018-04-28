package models.organize;

import models.BaseModel;
import models.area.Area;
import models.person.Person;
import models.token.BaseOrganize;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import vos.OrganizeVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
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
    
    public Organize parent() {
        return (Organize) this.parent;
    }
    
    
    
    public void move(Organize pre, Organize next) {
        if (pre == null || next == null) {
            if (pre == null) {
                this.rank = next.rank - 1;
            } else {
                this.rank = pre.rank + 1;
            }
        } else {
            this.rank = (pre.rank + next.rank) / 2;
        }
        this.save();
    }
    
    
    public Double initRank() {
        Double rank = Organize.find(defaultSql("select max(rank) from Organize where parent.id=?"), this.id).first();
        return rank == null ? 0 : rank + 1;
    }
    
    public boolean isRoot() {
        return this.parent == null && this.id.equals(this.source().id);
    }
    
    public Organize source() {
        return (Organize) this.organize;
    }
    
    public void source(Organize source) {
        this.organize = source;
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
    
    public static Organize findByID(Long id) {
        return Organize.find(defaultSql("id =?"), id).first();
    }
    
    public static Organize findBySourceAndName(Organize source, String name) {
        return Organize.find(defaultSql("organize=? and name = ?"), source, name).first();
    }
    
    public static List<Organize> fetchBySupervisor(Person supervisor) {
        if (supervisor == null) return Collections.EMPTY_LIST;
        return Organize.find(defaultSql("supervisor = ? and organize.id = ?"), supervisor, getSource()).fetch();
    }
    
    public static List<Organize> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Organize.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Organize> fetchAll() {
        return Organize.find(defaultSql()).fetch();
    }
    
    public List<Organize> children() {
        OrganizeVO organizeVO = new OrganizeVO();
        organizeVO.parentId = this.id;
        organizeVO.condition("order by rank");
        return Organize.fetch(organizeVO);
    }
    
    public static List<Organize> fetchBySource() {
        return Organize.find(defaultSql("organize.id=? order by rank"), getSource()).fetch();
    }
    
    public static List<Organize> fetchUpdate(Long updateTime, List<Long> organizeIds) {
        return Organize.find("updateTime>? and organize.id in (:organizeIds) order by updateTime", updateTime).bind("organizeIds", organizeIds.toArray()).fetch();
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