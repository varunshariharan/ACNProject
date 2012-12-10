import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/6/12
 * Time: 2:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserInputHandler implements Runnable{
    Router router;
    public UserInputHandler(Router router) {
        this.router = router;
    }

    @Override
    public void run() {
        System.out.println("User input thread Started");
        //To change body of implemented methods use File | Settings | File Templates.
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String inputString = bufferedReader.readLine();
                //write different cases here
                //1. send a message to a particular router
                //2. bring down the router
                //3. start sending hello messages
                //4. display routing table
//                String ip = null;//TODO get the ip from string and send to that ip
//                int portNumber = 0;
//                DatagramSocket clientSocket = new DatagramSocket();
//                InetAddress IPAddress = InetAddress.getByName(ip);
//                byte[] sendData = new byte[1024];
//                byte[] receiveData = new byte[1024];
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
//                clientSocket.send(sendPacket);
//                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                clientSocket.receive(receivePacket);
//                String modifiedSentence = new String(receivePacket.getData());
//                System.out.println("FROM SERVER:" + modifiedSentence);
//                clientSocket.close();
                if(inputString.equalsIgnoreCase("start")){
//                    router.started = true;
//                    HelloMessageSender helloMessageSender = new HelloMessageSender(router);
//                    Thread helloMessageThread = new Thread(helloMessageSender);
//                    helloMessageThread.start();
                    //send out routing info messages
//                    for (Integer neighbourRouterID : router.neighbours) {
//                        boolean sendAllEntries = false;
//                        router.displayRoutingTable();
//                        System.out.println("neighborID"+neighbourRouterID);
//                        if(router.routingTable.get(neighbourRouterID).get(neighbourRouterID).isGatewayRouter){
//                            sendAllEntries = true;
//                        }
//                        for (Map.Entry<Integer, Map<Integer, RoutingInfo>> routingEntries : router.routingTable.entrySet()) {
//                            for (Map.Entry<Integer, RoutingInfo> routingInfoEntry : routingEntries.getValue().entrySet()) {
//                                if ((!sendAllEntries && routingInfoEntry.getValue().internalEntry) || (sendAllEntries)){
//                                        Message pathMessage = new Message(Message.PATH,neighbourRouterID,router.routerId,routingInfoEntry.getValue(),router.currentSequenceNumber++,router.routerId);
//                                        router.sendMessage(pathMessage,neighbourRouterID);
//                                }
//                            }
//                        }
//                    }
                    router.start();
                }
                else if(inputString.equalsIgnoreCase("display")){
                    router.displayRoutingTable();
                }
                else {
                    String[] split = inputString.split(":");
                    int destinationRouter = Integer.parseInt(split[0]);
                    Message message = new Message(Message.DATA,destinationRouter,router.routerId,split[1],router.currentSequenceNumber++,router.routerId);
                    RoutingInfo routingInfo = router.routingTable.get(destinationRouter).get(0);
                    int sendToRouterID = routingInfo.nextHopRouterId;
                    router.sendMessage(message,sendToRouterID);
                }

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
