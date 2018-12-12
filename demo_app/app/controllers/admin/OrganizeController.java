package controllers.admin;

import annotations.ActionMethod;
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
    
    @ActionMethod(name = "机构列表", param = "page,size,-name,-personName,-personPhone", clazz = {PageData.class, OrganizeVO.class})
    public static void list(OrganizeVO vo) {
        int total = Organize.count(vo);
        List<Organize> organizes = Organize.fetch(vo);
        List<OrganizeVO> organizeVOs = organizes.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, organizeVOs)));
    }
    
    @ActionMethod(name = "机构详情", param = "organizeId", clazz = OrganizeVO.class)
    public static void info(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        OrganizeVO organizeVO = new OrganizeVO(organize);
        renderJSON(Result.succeed(organizeVO));
    }
    
    @ActionMethod(name = "机构新增", param = "name,logo,industry,employee,intro,startTime,endTime,person", clazz = OrganizeVO.class)
    public static void add(OrganizeVO vo) {
        Organize organize = Organize.init(vo);
        vo.person.type = PersonType.ORGANIZE.code();
        Person person = Person.add(vo.person);
        Relation.add(organize, person);
        organize.person(person);
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "机构编辑", param = "organizeId,-name,-logo,-industry,-employee,-intro,-startTime,-endTime,-person")
    public static void edit(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.edit(vo);
        if (vo.person != null) {
            Person person = organize.person();
            person.edit(vo.person);
        }
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "机构删除", param = "organizeId")
    public static void delete(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.del();
        renderJSON(Result.succeed());
    }
    
}
