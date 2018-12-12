package controllers.organize;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {

	@Before(priority = 100, unless = { "organize.Application.index", "organize.Application.version",
			"organize.Application.qiniuUptoken", "organize.Application.qiniuUptokenSimple", "organize.PersonController.login" })
	public static void access() {
		accesstoken();
	}
}