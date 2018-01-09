package controllers;

import jobs.ScheduleJob;
import play.cache.Cache;
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