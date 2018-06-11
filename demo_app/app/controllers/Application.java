package controllers;

import play.Logger;
import play.Play;
import play.db.jpa.NoTransaction;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        Logger.info(Play.applicationPath.getAbsolutePath());
        Logger.info(Play.getFile(Play.applicationPath.getAbsolutePath()).getAbsolutePath());
        renderHtml("start...");
    }
    
    public static void test() {
        renderJSON("test");
    }
}