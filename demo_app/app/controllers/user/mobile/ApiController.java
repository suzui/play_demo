package controllers.user.mobile;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {
    
    @Before(priority = 100, unless = {"user.mobile.Application.index", "user.mobile.Application.version","user.mobile.Application.download",
            "user.mobile.Application.configData", "user.mobile.Application.areaData", "user.mobile.Application.qiniuUptoken",
            "user.mobile.PersonController.captcha", "user.mobile.PersonController.regist", "user.mobile.PersonController.login", "user.mobile.PersonController.forgetPassword"})
    public static void access() {
        accesstoken();
    }
}
