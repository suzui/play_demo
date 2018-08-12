package controllers.admin;

import annotations.ActionMethod;
import models.access.Crowd;
import models.person.Person;
import vos.CrowdVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class CrowdController extends ApiController {
    
    @ActionMethod(name = "范围列表", param = "page,size", clazz = {PageData.class, CrowdVO.class})
    public static void crowdList(CrowdVO vo) {
        Person admin = getPersonByToken();
        int total = Crowd.count(vo);
        List<Crowd> crowds = Crowd.fetch(vo);
        List<CrowdVO> crowdVOs = crowds.stream().map(o -> new CrowdVO(o)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, crowdVOs)));
    }
    
    @ActionMethod(name = "范围详情", param = "crowdId", clazz = CrowdVO.class)
    public static void crowdInfo(CrowdVO vo) {
        Person admin = getPersonByToken();
        Crowd crowd = Crowd.findByID(vo.crowdId);
        CrowdVO crowdVO = new CrowdVO(crowd);
        crowdVO.organizes(crowd.organize());
        renderJSON(Result.succeed(crowdVO));
    }
    
    @ActionMethod(name = "范围新增", param = "name,organizeIds", clazz = CrowdVO.class)
    public static void crowdAdd(CrowdVO vo) {
        Person admin = getPersonByToken();
        Crowd crowd = Crowd.add(vo);
        renderJSON(Result.succeed(new CrowdVO(crowd)));
    }
    
    @ActionMethod(name = "范围编辑", param = "crowdId,-name,-organizeIds")
    public static void crowdEdit(CrowdVO vo) {
        Person admin = getPersonByToken();
        Crowd crowd = Crowd.findByID(vo.crowdId);
        crowd.edit(vo);
        renderJSON(Result.succeed(new CrowdVO(crowd)));
    }
    
    @ActionMethod(name = "范围删除", param = "crowdId")
    public static void crowdDelete(CrowdVO vo) {
        Person admin = getPersonByToken();
        Crowd crowd = Crowd.findByID(vo.crowdId);
        crowd.del();
        renderJSON(Result.succeed());
    }
    
}
