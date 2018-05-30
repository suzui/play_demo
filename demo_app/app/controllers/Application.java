package controllers;

import play.cache.Cache;
import play.db.jpa.NoTransaction;
import utils.CacheUtils;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    public static void test() {
        
        CacheUtils.add("b", "bbb");
        Cache.add("a", "aaa");
        
        renderJSON("test");
    }
}