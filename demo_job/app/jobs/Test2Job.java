package jobs;

import play.jobs.Every;

@Every("3s")
public class Test2Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
        //System.err.println("test2...");
    }
}
