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

    int messageType;
    String destinationRouter;
    String sourceRouter;
    String message;
    Route routingInfo;

    public static void main(String[] args) {
        String s = String.valueOf(5);
    }



}
