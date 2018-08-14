package models.organize;

import models.person.Person;
import models.token.BaseRelation;
import org.apache.commons.lang.StringUtils;
import vos.PersonVO;
import vos.RelationVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
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
        this.logicDelete();
    }
    
    public static Relation findByID(Long id) {
        return Relation.find(defaultSql("id=?"), id).first();
    }
    
    public static List<Relation> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Relation.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Relation> fetchAll() {
        return Relation.find(defaultSql()).fetch();
    }
    
    public static List<Relation> fetch(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Relation.find(defaultSql(StringUtils.join(hqls, " and ") + personVO.condition), params.toArray())
                .fetch(personVO.page, personVO.size);
    }
    
    public static int count(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Relation.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PersonVO personVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(personVO.search)) {
            hqls.add("concat_ws(',',person.name,person.phone,person.email) like ?");
            params.add("%" + personVO.search + "%");
        }
        return new Object[]{hqls, params};
    }
}