package controllers.organize;

import annotations.ActionMethod;
import enums.PersonType;
import models.organize.Organize;
import models.organize.Relation;
import models.person.Person;
import vos.PageData;
import vos.PersonVO;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class RelationController extends ApiController {
    
    @ActionMethod(name = "成员列表", param = "page,size,organizeId,-name,-phone", clazz = {PageData.class, PersonVO.class})
    public static void list(PersonVO vo) {
        int total = Relation.count(vo);
        List<Relation> relations = Relation.fetch(vo);
        List<PersonVO> personVOs = relations.stream().map(r -> new PersonVO(r).roles(r.person.roles())).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, personVOs)));
    }
    
    @ActionMethod(name = "成员详情", param = "organizeId,personId", clazz = PersonVO.class)
    public static void info(PersonVO vo) {
        Organize root = Organize.findByID(getRoot());
        Relation relation = Relation.findByOrganizeAndPerson(vo.organizeId, vo.personId);
        renderJSON(Result.succeed(new PersonVO(relation).roles(relation.person.roles()).organizes(relation.person.organizes(root))));
    }
    
    @ActionMethod(name = "成员添加", param = "name,idcard,phone,sex,-remark,-roleIds,organizeIds")
    public static void add(PersonVO vo) {
        Organize root = Organize.findByID(getRoot());
        vo.rootId = getRoot();
        vo.type = PersonType.ORGANIZE.code();
        Person person = Person.findByPhone(vo.phone, vo.type);
        if (person == null) {
            person = Person.add(vo);
        } else {
            person.edit(vo);
        }
        Relation.add(root, person);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "成员编辑", param = "personId,-name,-idcard,-phone,-sex,-remark,-roleIds,-organizeIds")
    public static void edit(PersonVO vo) {
        vo.rootId = getRoot();
        Person person = Person.findByID(vo.personId);
        person.edit(vo);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "成员删除", param = "organizeId,personId")
    public static void delete(PersonVO vo) {
        Relation.findByOrganizeAndPerson(vo.organizeId, vo.personId).del();
        renderJSON(Result.succeed());
    }
}
