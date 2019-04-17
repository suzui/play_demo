package controllers;

import utils.BaseUtils;

public class Application extends BaseController {
    
    public static void index() {
        System.err.println(BaseUtils.property(""));
        renderHtml("job");
    }
    
}