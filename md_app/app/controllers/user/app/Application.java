package controllers.user.app;

import annotations.ActionMethod;
import annotations.ParamField;
import enums.AppType;
import models.area.Area;
import models.person.Person;
import models.token.AccessToken;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Transactional;
import vos.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application extends ApiController {
    
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("app...");
    }
    
    @ActionMethod(name = "版本号详情", clazz = VersionVO.class)
    public static void version(@ParamField(name = "客户端类型") Integer clientType) {
        renderJSON(Result.succeed(new VersionVO(AppType.USER.code(), clientType).needUpdate()));
    }
    
    @ActionMethod(name = "客户端下载")
    public static void download(@ParamField(name = "客户端类型") Integer clientType) {
        VersionVO versionVO = new VersionVO(AppType.USER.code(), clientType);
        redirect(versionVO.downloadUrl);
    }
    
    @ActionMethod(name = "配置参数", clazz = ConfigVO.class)
    public static void configData() {
        ConfigVO configVO = new ConfigVO();
        renderJSON(Result.succeed(configVO));
    }
    
    @ActionMethod(name = "增量数据", clazz = IncrementVO.class)
    public static void incrementData(@ParamField(name = "需要增量对象", demo = "101:0,102:0,103:0") String param) {
        Person current = getPersonByToken();
        IncrementVO incrementVO = new IncrementVO();
        if (StringUtils.isBlank(param)) {
        } else {
            Pattern pattern = Pattern.compile("([\\d]+):([\\d]+)");
            for (String p : StringUtils.split(param, ",")) {
                Matcher matcher = pattern.matcher(p);
                if (matcher.find()) {
                    int type = Integer.valueOf(matcher.group(1));
                    long updateTime = Long.valueOf(matcher.group(2));
                    switch (type) {
                        case 101:
                            break;
                        case 102:
                            break;
                        case 103:
                            break;
                    }
                }
            }
        }
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
    
    
    @ActionMethod(name = "个推token")
    public static void getuiPushtoken(@ParamField(name = "推送token", comment = "安卓传clientId iOS传deviceToken") String pushtoken) {
        AccessToken accessToken = getAccessTokenByToken();
        accessToken.pushToken(pushtoken);
        renderJSON(Result.succeed());
    }
    
}
