package controllers;

import annotations.ActionMethod;
import models.person.Person;
import play.db.jpa.NoTransaction;
import utils.BaseUtils;
import vos.PersonVO;

import java.util.List;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        
        List<Person> persons = Person.fetchAll();
        System.err.println(BaseUtils.modelToId(persons));
        renderJSON("test");
    }
    
}