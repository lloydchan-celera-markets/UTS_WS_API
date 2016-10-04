import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
* Hello World server
* Connects REP socket to tcp://*:5560
* Expects "Hello" from client, replies with "World"
*
* Christophe Huntzinger <chuntzin_at_wanadoo.fr>
*/
public class rrserver{
    public static void main (String[] args) {
        Context context = ZMQ.context(1);

        //  Socket to talk to clients
        Socket responder  = context.socket(ZMQ.REP);
        responder.connect("tcp://localhost:9998");
        
        System.out.println("launch and connect server.");
	try
	{

        //while (!Thread.currentThread().isInterrupted()) {
        while (true) {
            //  Wait for next request from client
            byte[] request = responder.recv(0);
            String string = new String(request);
            System.out.println("Received request: ["+string+"].");

            //  Do some 'work'
            //try {
            	responder.send("World".getBytes(), 0);
                Thread.sleep(1);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}

            //  Send reply back to client
            //responder.send("World".getBytes(), 0);
        }
        }
	catch (InterruptedException e) {
                e.printStackTrace();
            }

        //  We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}
