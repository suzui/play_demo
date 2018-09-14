package controllers;

import annotations.ActionMethod;
import play.Play;
import play.db.jpa.NoTransaction;
import utils.BaseUtils;
import vos.PersonVO;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        System.err.println(Play.id);
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        String a = BaseUtils.property("a");
        String b = BaseUtils.property("b");
        System.err.println(a);
        System.err.println(b);
        renderJSON("test");
    }
    
}