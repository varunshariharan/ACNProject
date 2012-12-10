import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
        System.out.println("Message handler Started");
        BlockingQueue<DatagramPacket> messageQueue = this.router.packetQueue;
        while(true){
            try {
                DatagramPacket packet = messageQueue.take();
                //convert packet to message
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                Message receivedMessage = (Message)in.readObject();
                //process message
                System.out.println("Received "+receivedMessage.messageType+" message from "+receivedMessage.sourceRouter);
                int receivedFromRouter = receivedMessage.sendingRouter;
                boolean amIAGatewayRouter = false;
                boolean isReceivedRouterGatewayRouter = router.routingTable.get(receivedFromRouter).get(receivedFromRouter).isGatewayRouter;
                if(router.routerType == 'g')
                    amIAGatewayRouter = true;
                if(receivedMessage.messageType == Message.HELLO || receivedMessage.messageType == Message.ACK){
                    //send ack
                    if (receivedMessage.messageType == Message.HELLO){
                        Message ack = new Message(Message.ACK,receivedMessage.sendingRouter,router.routerId,receivedMessage.sequenceNumber,router.routerId);
                        router.sendMessage(ack,receivedMessage.sendingRouter);
                    }
                    //set timeout table values
                    router.timeoutTable.put(receivedMessage.sendingRouter,new AtomicInteger(3));
                    router.start();
                }
                else if (receivedMessage.messageType == Message.PATH_REQUEST){
                    //send path to person who asked
                    int pathDestination = Integer.parseInt(receivedMessage.message);
                    Map<Integer,RoutingInfo> routingInfoMap = router.routingTable.get(pathDestination);
//                    Map<Integer, RoutingInfo> resultRoutingInfoMap = new HashMap<Integer, RoutingInfo>();
//                    int minHopRouter;
                    int minHops = 99999;
                    RoutingInfo routingInfo = null;
                    for (Map.Entry<Integer, RoutingInfo> routingInfoEntry : routingInfoMap.entrySet()) {
                        routingInfo = routingInfoEntry.getValue();
                        if(minHops > routingInfo.numberOfHops){
                            minHops = routingInfo.numberOfHops;
//                            minHopRouter = routingInfoEntry.getKey();
                        }
                    }
                    RoutingInfo newRoutingInfo = new RoutingInfo(receivedMessage.destinationRouter,router.routerId,(routingInfo.numberOfHops)+1,amIAGatewayRouter,!(amIAGatewayRouter && isReceivedRouterGatewayRouter));
                    Message pathMessage = new Message(Message.PATH,receivedFromRouter,router.routerId,router.currentSequenceNumber++,router.routerId);
                    router.sendMessage(pathMessage,receivedFromRouter);
                }
                else if (receivedMessage.messageType == Message.PATH){
                    //update routing table
                    //send this path if it came from internal router to all neighbors except the one who sent it
                    //check if Path is received from gateway router, then send only to other gateway routers
                    RoutingInfo routingInfo = receivedMessage.routingInfo;
                    if(router.routingTable.containsKey(routingInfo.destinationRouterId)){
                        Map<Integer, RoutingInfo> routingInfoMap = router.routingTable.get(routingInfo.destinationRouterId);
                        if(!routingInfoMap.containsKey(receivedFromRouter)){
                            routingInfoMap.put(receivedFromRouter,routingInfo);
                        }
                        else {
                            routingInfoMap.get(receivedFromRouter).numberOfHops = routingInfo.numberOfHops;
                        }
                    }

                    if(!isReceivedRouterGatewayRouter){
                        //send to all routers
                    }
                    else {
                        //send only to gateway routers
                    }
                }
                else if (receivedMessage.messageType == Message.WITHDRAW){
                    //remove corresponding entry from routing table and send withdraw to all neighbors. Follow gateway router rules.
                }
                else if (receivedMessage.messageType == Message.DATA){
                    //check if you are the destination. If yes, print out message received.
                    //else forward the message according to entry in the routing table if present
                    //      if no entry in table, send out path requests to all other neighbors and then update routing table and send the message across
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
