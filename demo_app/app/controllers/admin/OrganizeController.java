package controllers.admin;

import annotations.ActionMethod;
import models.organize.Organize;
import vos.OrganizeVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class OrganizeController extends ApiController {
    
    @ActionMethod(name = "机构列表", param = "page,size", clazz = {PageData.class, OrganizeVO.class})
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
    
    @ActionMethod(name = "机构新增", param = "name,personName,personPhone", clazz = OrganizeVO.class)
    public static void add(OrganizeVO vo) {
        Organize organize = Organize.add(vo);
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "机构编辑", param = "organizeId,name")
    public static void edit(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.edit(vo);
        renderJSON(Result.succeed(new OrganizeVO(organize)));
    }
    
    @ActionMethod(name = "机构删除", param = "organizeId")
    public static void delete(OrganizeVO vo) {
        Organize organize = Organize.findByID(vo.organizeId);
        organize.del();
        renderJSON(Result.succeed());
    }
    
}
