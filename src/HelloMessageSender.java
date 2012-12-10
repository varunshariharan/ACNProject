/**
 * Created with IntelliJ IDEA.
 * User: varunhariharan
 * Date: 12/8/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloMessageSender implements Runnable{
    Router router;
    public HelloMessageSender(Router router) {
        this.router = router;
    }

    @Override
    public void run() {
        System.out.println("Hello Message sender Started");
        while(true){
            try {
                Thread.sleep(4000);
                for (Integer neighborRouterID : router.neighbours) {
                    //get IP address and send message
                    Message helloMessage = new Message(Message.HELLO,neighborRouterID,router.routerId,router.currentSequenceNumber++,router.routerId);
                    router.sendMessage(helloMessage,neighborRouterID);
                        //write logic to get ack, if no ack then retransmit
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
