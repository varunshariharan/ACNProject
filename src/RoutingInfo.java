import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoutingInfo implements Serializable {
    int destinationRouterId;
    boolean isGatewayRouter;
    int numberOfHops;
    int nextHopRouterId;
    boolean internalEntry;

    public RoutingInfo(int destinationRouterId, int nextHopRouterId, int numberOfHops, boolean isGatewayRouter, boolean internalEntry) {
        this.destinationRouterId = destinationRouterId;
        this.nextHopRouterId = nextHopRouterId;
        this.isGatewayRouter = isGatewayRouter;
        this.numberOfHops = numberOfHops;
        this.internalEntry = internalEntry;
    }
}
