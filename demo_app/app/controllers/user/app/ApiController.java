package controllers.user.app;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {
    
    @Before(priority = 100, unless = {"user.app.Application.index", "user.app.Application.version", "user.app.Application.download",
            "user.app.Application.configData", "user.app.Application.areaData", "user.app.Application.qiniuUptoken",
            "user.app.PersonController.captcha", "user.app.PersonController.regist", "user.app.PersonController.login", "user.app.PersonController.forgetPassword"})
    public static void access() {
        accesstoken();
    }
}
