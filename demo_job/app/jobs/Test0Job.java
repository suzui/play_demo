package jobs;

import javafx.concurrent.Task;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Test0Job extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        super.doJob();
        //System.err.println("test0...");
    }
    
    public static void main(String[] args) {
        Test0Job job = new Test0Job();
        Task t = new Task() {
            
            @Override
            protected Object call() throws Exception {
                return null;
            }
        };
    }
}
