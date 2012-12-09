/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/4/12
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Message {
    public static int HELLO = 1;
    public static int PATH = 2;
    public static int PATH_REQUEST = 3;
    public static int DATA = 4;
    public static int WITHDRAW = 5;
    public static int ERROR = 6;
    public static int ACK = 7;

    int messageType;
    int destinationRouter;
    int sourceRouter;
    String message;
    Route routingInfo;

    public Message(int messageType, int destinationRouter, int sourceRouter) {
        this.messageType = messageType;
        this.destinationRouter = destinationRouter;
        this.sourceRouter = sourceRouter;
    }

    public Message(int messageType, int destinationRouter, int sourceRouter, Route routingInfo) {
        this.messageType = messageType;
        this.destinationRouter = destinationRouter;
        this.sourceRouter = sourceRouter;
        this.routingInfo = routingInfo;
    }

    public Message(int messageType, int destinationRouter, int sourceRouter, String message) {
        this.messageType = messageType;
        this.destinationRouter = destinationRouter;
        this.sourceRouter = sourceRouter;
        this.message = message;
    }
}

