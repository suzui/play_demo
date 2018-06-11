package controllers;

import play.Play;
import play.db.jpa.NoTransaction;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        System.err.println(Play.getFile(Play.applicationPath.getAbsolutePath()).getParent());
        
        renderHtml("start...");
    }
    
    public static void test() {
        renderJSON("test");
    }
}