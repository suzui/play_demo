package jobs;

import play.jobs.On;

@On("*/3 * * * * ?")
public class Test3Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
        //System.err.println("test3...");
    }
}
