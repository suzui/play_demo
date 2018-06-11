package jobs;

import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Test1Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
        //System.err.println("test1...");
    }
}
