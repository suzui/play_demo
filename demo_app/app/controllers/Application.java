package controllers;

import enums.ProStatus;
import models.back.Pro;
import play.db.jpa.NoTransaction;

import java.util.List;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    public static void test() {
        List<Pro> pros = Pro.fetchAll();
        pros.stream().filter(p -> p.location.contains("app")).forEach(p -> {
            if (p.check().read.contains("java")) {
                p.status(ProStatus.NORMAL);
            } else {
                p.status(ProStatus.STOP);
            }
        });
        renderJSON("test");
    }
    
}