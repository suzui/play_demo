package controllers.user.h5;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {
    
    @Before(priority = 100, unless = {})
    public static void access() {
        accesstoken();
    }
}
