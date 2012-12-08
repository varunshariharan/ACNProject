/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoutingInfo {
    int destinationRouterId;
    boolean isGatewayRouter;
    int numberOfHops;
    int nextHopRouterId;

    public RoutingInfo(int destinationRouterId, int nextHopRouterId, boolean isGatewayRouter) {
        this.destinationRouterId = destinationRouterId;
        this.nextHopRouterId = nextHopRouterId;
        this.isGatewayRouter = isGatewayRouter;
    }
}
