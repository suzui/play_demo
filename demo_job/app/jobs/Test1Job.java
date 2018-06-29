package jobs;

import play.jobs.OnApplicationStart;

@OnApplicationStart(async = true)
public class Test1Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
    }
}
