package controllers;

import annotations.ActionMethod;
import enums.AccessType;
import models.access.Access;
import models.access.BaseAccess;
import play.db.jpa.NoTransaction;
import vos.PersonVO;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        Access access = Access.add("001", "", AccessType.ADMIN);
        BaseAccess.add("002", "", AccessType.ADMIN);
        
        System.err.println(Access.findAll().size());
        renderJSON("test");
    }
    
}