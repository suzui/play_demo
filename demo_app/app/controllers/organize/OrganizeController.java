package controllers.organize;

import annotations.ActionMethod;
import enums.OrganizeType;
import enums.PersonType;
import models.organize.Organize;
import models.organize.Relation;
import models.person.Person;
import vos.OrganizeVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class OrganizeController extends ApiController {
    
    @ActionMethod(name = "组织树", clazz = OrganizeVO.class)
    public static void tree(OrganizeVO vo) {
        Organize root = Organize.findByID(getRoot());
        renderJSON(Result.succeed(OrganizeVO.tree(root)));
    }
    
    @ActionMethod(name = "组织列表", param = "page,size,-parentId,-name", clazz = {PageData.class, OrganizeVO.class})
    public static void list(OrganizeVO vo) {
        if (vo.parentId == null) {
            vo.parentId = getRoot();
        }
        int total = Organize.count(vo);
        List<Organize> organizes = Organize.fetch(vo);
        List<OrganizeVO> organizeVOs = organizes.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, organizeVOs)));
    }
    
    @ActionMethod(name = "组织详情", param = "organizeId", clazz = OrganizeVO.class)
    public static void info(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        OrganizeVO organizeVO = new OrganizeVO(organize);
        organizeVO.children(Organize.fetchByParent(organize));
        renderJSON(Result.succeed(organizeVO));
    }
    
    @ActionMethod(name = "组织新增", param = "parentId,type,name,-unit,-number,-startTime,-endTime,-image,-address,-intro,person", clazz = OrganizeVO.class)
    public static void add(OrganizeVO vo) {
        vo.rootId = getRoot();
        Organize organize = Organize.add(vo);
        vo.person.type = PersonType.ORGANIZE.code();
        Person person = Person.findByPhone(vo.person.phone, vo.person.type);
        if (person == null) {
            person = Person.add(vo.person);
        } else {
            person.edit(vo.person);
        }
        Relation.add(organize.root(), person);
        Relation.add(organize, person);
        organize.person(person);
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "组织编辑", param = "organizeId,-parentId,-name,-unit,-number,-startTime,-endTime,-image,-address,-intro,-person")
    public static void edit(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.edit(vo);
        organize.move(vo.parentId);
        vo.person.type = PersonType.ORGANIZE.code();
        Person person = Person.findByPhone(vo.person.phone, vo.person.type);
        if (person == null) {
            person = Person.add(vo.person);
        } else {
            person.edit(vo.person);
        }
        Relation.add(organize.root(), person);
        Relation.add(organize, person);
        organize.person(person);
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "组织删除", param = "organizeId")
    public static void delete(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.del();
        renderJSON(Result.succeed());
    }
    
}
