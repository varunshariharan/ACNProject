import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/7/12
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler implements Runnable{
    Router router;

    public MessageHandler(Router router) {
        this.router = router;
    }

    @Override
    public void run() {
        BlockingQueue<DatagramPacket> messageQueue = this.router.packetQueue;
        while(true){
            try {
                DatagramPacket packet = messageQueue.take();
                //convert packet to message
                System.out.println("Message caught from queue");
                //process message

            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
