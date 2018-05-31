package controllers;

import models.person.Person;
import play.db.jpa.NoTransaction;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    public static void test() {
        Person person = Person.findByID(0l);
        long id = person.id;
        renderJSON("test");
    }
}