package jobs;

import models.area.Area;
import org.hibernate.Session;
import play.db.jpa.JPA;
import play.jobs.OnApplicationStart;

@OnApplicationStart(async = true)
public class InitAreaJob extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        //initArea();
    }
    
    private static void initArea() {
        final Session s = (Session) JPA.em().getDelegate();
        if (!s.getTransaction().isActive()) {
            s.getTransaction().begin();
        }
        Area.init();
        s.getTransaction().commit();
    }
    
}
