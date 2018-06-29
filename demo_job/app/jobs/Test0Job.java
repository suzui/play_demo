package jobs;

import play.jobs.Every;
import play.jobs.OnApplicationStart;

@OnApplicationStart
@Every("3s")
public class Test0Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
    }
    
    
}
