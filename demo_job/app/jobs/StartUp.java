package jobs;

import models.person.Person;
import org.hibernate.Session;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import javax.persistence.EntityManager;

@OnApplicationStart
public class StartUp extends Job {
    
    @Override
    public void doJob() throws Exception {
        initAdmin();
        updateColumn();
    }
    
    private static void initAdmin() {
        final Session s = (Session) JPA.em().getDelegate();
        if (!s.getTransaction().isActive()) {
            s.getTransaction().begin();
        }
        Person.initAdmin();
        s.getTransaction().commit();
    }
    
    private static void updateColumn() {
        EntityManager em = JPA.em();
        Session s = (Session) em.getDelegate();
        if (!s.getTransaction().isActive())
            s.getTransaction().begin();
        s.getTransaction().commit();
    }
}
