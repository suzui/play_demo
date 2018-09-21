package controllers;

import annotations.ActionMethod;
import play.Play;
import play.db.jpa.NoTransaction;
import utils.HolidayUtils;
import vos.PersonVO;

import java.util.Date;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        System.err.println(Play.id);
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        HolidayUtils.yearMonth(new Date());
        renderJSON("test");
    }
    
    
    
    
}