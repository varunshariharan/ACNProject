import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
        //To change body of implemented methods use File | Settings | File Templates.
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String inputString = bufferedReader.readLine();
                //write different cases here
                //1. send a message to a particular router
                //2. bring down the router
                String ip = null;//TODO get the ip from string and send to that ip
                int portNumber = 0;
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName(ip);
                byte[] sendData = new byte[1024];
                byte[] receiveData = new byte[1024];

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println("FROM SERVER:" + modifiedSentence);
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
