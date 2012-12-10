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
        System.out.println("Timeout Monitor Started");
        while(true){
            try {
                Thread.sleep(5000);
                System.out.println("Checking");
                //scan table and decrement counts
                Map<Integer,AtomicInteger> timeoutTable = router.timeoutTable;
                for (Integer helloMessageCheckRouterID : timeoutTable.keySet()) {
                    AtomicInteger count = timeoutTable.get(helloMessageCheckRouterID);
                    if(count.decrementAndGet()==0){
                        //remove entry from router table
                        if(router.routingTable.containsKey(helloMessageCheckRouterID))                                               {
                            RoutingInfo remove = router.routingTable.get(helloMessageCheckRouterID).remove(helloMessageCheckRouterID);
                            System.out.println("Router down. RouterID : "+helloMessageCheckRouterID);
                        }
                        //TODO send routingInfo to neighbours
                        for (Integer neighbourRouterID : router.neighbours) {
                            Message withdrawMessage = new Message(Message.WITHDRAW,neighbourRouterID,router.routerId,String.valueOf(helloMessageCheckRouterID),router.currentSequenceNumber++,router.routerId);
                            router.sendMessage(withdrawMessage,neighbourRouterID);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
