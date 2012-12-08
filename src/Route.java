/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Route {
    int destinationRouterId;
    boolean isGatewayRouter;
    int numberOfHops;

    public Route(RoutingInfo routingInfo) {
        this.destinationRouterId = routingInfo.destinationRouterId;
        this.isGatewayRouter = routingInfo.isGatewayRouter;
        this.numberOfHops = routingInfo.numberOfHops;
    }
}
