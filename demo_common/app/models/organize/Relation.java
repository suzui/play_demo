package models.organize;

import models.person.Person;
import models.token.BaseRelation;
import org.apache.commons.lang.StringUtils;
import vos.PersonVO;
import vos.RelationVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Relation extends BaseRelation {
    
    public static Relation add(Organize organize, Person person) {
        if (organize == null || person == null) {
            return null;
        }
        Relation relation = findByOrganizeAndPerson(organize, person);
        if (relation != null) {
            return relation;
        }
        relation = new Relation();
        relation.organize = organize;
        relation.person = person;
        relation.rank = relation.initRank();
        return relation.save();
    }
    
    public Relation edit(RelationVO vo) {
        this.rank = vo.rank != null ? vo.rank : rank;
        return this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Relation> fetch(PersonVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Relation.find(defaultSql(StringUtils.join(hqls, " and ") + vo.condition), params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(PersonVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Relation.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PersonVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.search)) {
            hqls.add("concat_ws(',',person.name,person.phone,person.email) like ?");
            params.add("%" + vo.search + "%");
        }
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add("person.name like ?");
            params.add("%" + vo.name + "%");
        }
        if (StringUtils.isNotBlank(vo.phone)) {
            hqls.add("person.phone like ?");
            params.add("%" + vo.phone + "%");
        }
        if (vo.organizeId != null) {
            hqls.add("organize.id = ?");
            params.add(vo.organizeId);
        }
        return new Object[]{hqls, params};
    }
}