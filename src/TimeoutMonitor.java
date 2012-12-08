import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/7/12
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeoutMonitor implements Runnable{
    Router router;
    public TimeoutMonitor(Router router) {
        this.router = router;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(5000);
                //scan table and decrement counts
                Map<Integer,AtomicInteger> timeoutTable = router.timeoutTable;
                for (Integer routerID : timeoutTable.keySet()) {
                    AtomicInteger count = timeoutTable.get(routerID);
                    if(count.decrementAndGet()==0){
                        //remove entry from router table
                        router.routingTable.remove(routerID);
                        //TODO send routingInfo to neighbours

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
