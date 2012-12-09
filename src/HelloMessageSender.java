import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/8/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloMessageSender implements Runnable{
    Router router;
    public HelloMessageSender(Router router) {
        this.router = router;
    }

    @Override
    public void run() {
        for (Integer neighborRouterID : router.neighbours) {
            //get IP address and send message
            String address = router.routerToIpMap.get(neighborRouterID);
            String[] split = address.split(":");
            Message helloMessage = new Message(Message.HELLO,neighborRouterID,router.routerId);
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName(split[0]);
                router.sendMessage(helloMessage,socket,IPAddress, Integer.parseInt(split[1]));
                //write logic to get ack, if no ack then retransmit
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
