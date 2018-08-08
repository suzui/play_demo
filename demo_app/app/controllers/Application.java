package controllers;

import annotations.ActionMethod;
import models.BaseModel;
import models.person.Person;
import play.db.jpa.NoTransaction;
import vos.PersonVO;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", param = "personId,name,type", clazz=PersonVO.class)
    public static void test(PersonVO vo) {
        renderJSON("test");
    }
    
}