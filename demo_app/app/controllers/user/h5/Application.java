package controllers.user.h5;

import controllers.user.app.ApiController;
import play.db.jpa.Transactional;

public class Application extends ApiController {
    
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("h5...");
    }
    
    
}
