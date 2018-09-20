package controllers;

import annotations.ActionMethod;
import play.Play;
import play.db.jpa.NoTransaction;
import vos.PersonVO;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        System.err.println(Play.id);
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        vo.size = 100;
        System.err.println(vo.size);
        t(vo);
        System.err.println(vo.size);
        renderJSON("test");
    }
    
    private static void t(PersonVO vo) {
        vo.size = 101;
    }
    
    
    
}