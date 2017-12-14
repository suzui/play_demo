package controllers.user;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {

	@Before(priority = 100, unless = { "user.Application.index", "user.Application.version",
			"user.Application.qiniuUptoken", "user.Application.qiniuUptokenSimple", "user.AdminController.captcha",
			"user.AdminController.regist", "user.AdminController.login", "user.AdminController.forgetPassword" })
	public static void access() {
		accesstoken();
	}
}