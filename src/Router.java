import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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
    char routerType;
    Map<Integer,String> routeToIpMap = new HashMap<Integer, String>();
    RoutingTable routingTable;
    int domainId;
    Set<Integer> neighbours = new HashSet<Integer>();

    public Router(int routerId, String routerString, String ip, int portNumber){
        this.routerId = routerId;
        this.routerString = routerString;
        this.ip = ip;
        this.portNumber = portNumber;
        try {
            readConfigFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void readConfigFile() throws IOException {
        File configFile = new File("./config.txt");
        List<String> strings = FileUtils.readLines(configFile);
        for (String line : strings) {
            if(line.contains("(")){
                String[] split = line.split("\\)\\(");
                int count = 0;
                split[0] = split[0].replace("(","");
                String[] firstEntry = split[0].split(",");
                if(Integer.parseInt(firstEntry[0]) == this.routerId){
                    for (int i = 1, splitLength = split.length; i < splitLength; i++) {
                        String entry = split[i];
                        entry.replaceAll("\\(", "").replaceAll("\\)", "");
                        String[] entrySplit = entry.split(",");
                        int neighborRouterId = Integer.parseInt(entrySplit[0]);
                        neighbours.add(neighborRouterId);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Router router = new Router(Integer.parseInt(args[0]),args[1],InetAddress.getLocalHost().getHostAddress().toString(),Integer.parseInt(args[2]));
        System.out.println(router.neighbours.toString());
    }
}
