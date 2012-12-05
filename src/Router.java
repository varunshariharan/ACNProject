import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Router {
    String routerId;
    String ip;
    int portNumber;
    char routerType;
    Map<Integer,String> routeToIpMap = new HashMap<Integer, String>();
    RoutingTable routingTable;
    int domainId;
}
