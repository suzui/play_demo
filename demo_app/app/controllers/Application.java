package controllers;

import annotations.ActionMethod;
import play.db.jpa.NoTransaction;
import vos.PersonVO;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        renderJSON("test");
    }
    
}