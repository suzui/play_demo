package controllers;

import annotations.ActionMethod;
import models.organize.Organize;
import models.person.Person;
import models.token.BasePerson;
import net.sf.ehcache.hibernate.EhCacheProvider;
import play.db.jpa.NoTransaction;
import vos.PersonVO;

import java.util.List;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    @ActionMethod(name = "测试", clazz = PersonVO.class)
    public static void test(PersonVO vo) {
        List<Organize> list = Organize.fetchAll();
        BasePerson person = list.get(1).person();
        System.err.println(person);
        //System.err.println(person.area);
        //Person person = Person.findByID(1l);
        //System.err.println(person.area);
        Organize organize = Organize.findByID(2l);
        //Organize.findByID(2l);
        //Person person = organize.person();
        System.err.println(organize.person);
        renderJSON("test");
    }
    
    
}