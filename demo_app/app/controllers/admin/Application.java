package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import enums.AppType;
import enums.ClientType;
import models.area.Area;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Transactional;
import vos.*;

public class Application extends ApiController {
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("admin...");
    }
    
    @ActionMethod(name = "版本号详情", clazz = VersionVO.class)
    public static void version(@ParamField(name = "客户端类型") Integer clientType) {
        renderJSON(Result.succeed(new VersionVO(AppType.ADMIN, ClientType.convert(clientType))));
    }
    
    @ActionMethod(name = "配置参数", clazz = ConfigVO.class)
    public static void configData() {
        ConfigVO configVO = new ConfigVO();
        renderJSON(Result.succeed(configVO));
    }
    
    @ActionMethod(name = "增量数据", clazz = IncrementVO.class)
    public static void incrementData() {
        IncrementVO incrementVO = new IncrementVO();
        renderJSON(Result.succeed(incrementVO));
    }
    
    @ActionMethod(name = "地区数据", clazz = AreaVO.class)
    public static void areaData(@ParamField(name = "地址code", required = false) String code) {
        AreaVO areaVO = null;
        if (StringUtils.isBlank(code)) {
            areaVO = AreaVO.tree();
        } else {
            Area area = Area.findByCode(code);
            areaVO = new AreaVO(area);
            areaVO.children(Area.fetchByParent(area));
        }
        renderJSON(Result.succeed(areaVO));
    }
    
    @ActionMethod(name = "七牛token", clazz = QiniuVO.class)
    public static void qiniuUptoken() {
        renderJSON(Result.succeed(new QiniuVO()));
    }
    
    public static void qiniuUptokenSimple() {
        renderJSON(new QiniuVO());
    }
    
}
