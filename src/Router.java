import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Router {
    int routerId;
    String routerString;
    String ip;
    int portNumber;
    BlockingQueue<DatagramPacket> packetQueue = new LinkedBlockingQueue<DatagramPacket>();
    char routerType;
    Map<Integer,String> routerToIpMap = new HashMap<Integer, String>(); //routerID to routerIP
    Map<String,Integer> routerStringToIDMap = new HashMap<String, Integer>(); //routerString to routerID
    Map<Integer,Map<Integer,RoutingInfo>> routingTable = new HashMap<Integer, Map<Integer, RoutingInfo>>(); //routerID to routingInfo
    Map<Integer,AtomicInteger> timeoutTable = new HashMap<Integer, AtomicInteger>(); //routerID to timeoutCount
    int currentSequenceNumber;
    Set<Integer> neighbours = new HashSet<Integer>();
    boolean started = false;

    public Router(String routerString, String ip, int portNumber){
        this.currentSequenceNumber = 1;
        this.ip = ip;
//        this.routerId = routerId;
        this.routerString = routerString;
        this.portNumber = portNumber;
        try {
            readConfigFile();
            //spawn userInputHandler thread
            UserInputHandler userInputHandler = new UserInputHandler(this);
            Thread userInputThread = new Thread(userInputHandler);
            userInputThread.start();

            //start message handler thread
            MessageHandler messageHandler = new MessageHandler(this);
            Thread messageThread = new Thread(messageHandler);
            messageThread.start();

            //start timeoutHandler thread
            TimeoutMonitor timeoutMonitor = new TimeoutMonitor(this);
            Thread timeoutThread = new Thread(timeoutMonitor);
            timeoutThread.start();

            //look for new messages and if message received, put it in a queue
            DatagramSocket serverSocket = new DatagramSocket(portNumber);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket;
            while(true){
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                packetQueue.add(receivePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void readConfigFile() throws IOException {
        File configFile = new File("./config copy.txt");
        List<String> strings = FileUtils.readLines(configFile);
        for (String line : strings) {
            if(line.contains("(")){
                String[] split = line.split("\\)\\(");
                int count = 0;
                split[0] = split[0].replace("(","");
                String[] firstEntry = split[0].split(",");
                if(firstEntry[1].equalsIgnoreCase(this.routerString)){
                    this.routerType = firstEntry[2].charAt(0);
                    this.routerId = Integer.parseInt(firstEntry[0]);
                    RoutingInfo routingInfo;
                    boolean isRouterGatewayRouter;
                    Map<Integer,RoutingInfo> routingInfoMap;
                    for (int i = 1, splitLength = split.length; i < splitLength; i++) {
                        String entry = split[i];
                        entry.replaceAll("\\(", "").replaceAll("\\)", "");
                        String[] entrySplit = entry.split(",");
                        int neighborRouterId = Integer.parseInt(entrySplit[0]);
                        neighbours.add(neighborRouterId);

                        if(entrySplit[2].equalsIgnoreCase("g"))
                            isRouterGatewayRouter = true;
                        else
                            isRouterGatewayRouter = false;

                        boolean internalEntry = true;
                        if (this.routerType == 'g' && isRouterGatewayRouter)
                            internalEntry = false;

                        routingInfo = new RoutingInfo(neighborRouterId,neighborRouterId,1,isRouterGatewayRouter,internalEntry);
                        routingInfoMap = new HashMap<Integer,RoutingInfo>();
                        routingInfoMap.put(neighborRouterId, routingInfo);
                        routingTable.put(neighborRouterId,routingInfoMap);
                    }
                }
            }
        }

        for (String line : strings) {
            if(line.contains("(")){
                String[] split = line.split("\\)\\(");
                split[0] = split[0].replace("(","");
                String[] firstEntry = split[0].split(",");
                int id = Integer.parseInt(firstEntry[0]);
                String routerString = firstEntry[1];
                if(!routerStringToIDMap.containsKey(routerString)){
                    routerStringToIDMap.put(routerString,id);
                }
            }
        }

        File ipConfigFile = new File("./ipconfig.txt");
        List<String> routerIPList = FileUtils.readLines(ipConfigFile);
        for (String line : routerIPList) {
            String[] split = line.split(" ");
            if(!routerStringToIDMap.containsKey(split[0])){
                System.out.println(split[0]);
                System.out.println("Router IPconfig file error");
                System.exit(1);
            }
            else {
                routerToIpMap.put(routerStringToIDMap.get(split[0]),split[1]);
            }
        }

    }

    public void sendMessage(Message message, int sendToRouterID) throws IOException {
        String address = routerToIpMap.get(sendToRouterID);
        String[] split = address.split(":");
        DatagramSocket socket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(split[0]);
        int portNumber = Integer.parseInt(split[1]);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out=new ObjectOutputStream(bos);
        out.writeObject(message);
        out.flush();
        byte[] byteObject = bos.toByteArray();
        DatagramPacket packet=new DatagramPacket(byteObject, byteObject.length,IPAddress,portNumber);
        socket.send(packet);

        //TODO write logic for ACKs

        socket.close();
    }

    public void displayRoutingTable(){
        System.out.println("DestinationRouter\tNextHop\tNoOfHops\tisGatewayRouter");
        for (Map.Entry<Integer, Map<Integer, RoutingInfo>> entry : routingTable.entrySet()) {
            Map<Integer, RoutingInfo> routingInfoMap = entry.getValue();
            for (Map.Entry<Integer, RoutingInfo> routingInfoEntry : routingInfoMap.entrySet()) {
                RoutingInfo routingInfo = routingInfoEntry.getValue();
                System.out.println(routingInfo.destinationRouterId+"\t"+routingInfo.nextHopRouterId+"\t"+routingInfo.numberOfHops+"\t"+routingInfo.isGatewayRouter);
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Router router = new Router(args[0],InetAddress.getLocalHost().getHostAddress().toString(),Integer.parseInt(args[1]));
        System.out.println(router.neighbours.toString());
    }

    public void start() throws IOException {
        this.started = true;
        HelloMessageSender helloMessageSender = new HelloMessageSender(this);
        Thread helloMessageThread = new Thread(helloMessageSender);
        helloMessageThread.start();
        //send out routing info messages
        for (Integer neighbourRouterID : this.neighbours) {
            boolean sendAllEntries = false;
            this.displayRoutingTable();
            System.out.println("neighborID"+neighbourRouterID);
            if(this.routingTable.get(neighbourRouterID).get(neighbourRouterID).isGatewayRouter){
                sendAllEntries = true;
            }
            for (Map.Entry<Integer, Map<Integer, RoutingInfo>> routingEntries : this.routingTable.entrySet()) {
                for (Map.Entry<Integer, RoutingInfo> routingInfoEntry : routingEntries.getValue().entrySet()) {
                    if ((!sendAllEntries && routingInfoEntry.getValue().internalEntry) || (sendAllEntries)){
                        Message pathMessage = new Message(Message.PATH,neighbourRouterID,this.routerId,routingInfoEntry.getValue(),this.currentSequenceNumber++,this.routerId);
                        sendMessage(pathMessage, neighbourRouterID);
                    }
                }
            }
        }
    }
}
