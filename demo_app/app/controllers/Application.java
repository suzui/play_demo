package controllers;

import play.db.jpa.NoTransaction;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    public static void test() {
        renderJSON("test");
    }
}