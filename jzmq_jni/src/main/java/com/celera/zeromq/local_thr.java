package com.celera.zeromq;
import org.zeromq.ZMQ;

class local_thr
{
    public static void main (String [] args)
    {
        if (args.length != 3) {
            System.out.println ("usage: local_thr <bind-to> " +
                                "<message size> <message count>");
            return;
        }

        String bindTo = args [0];
        long messageSize = Integer.parseInt (args [1]);
        long messageCount = Integer.parseInt (args [2]);

        ZMQ.Context ctx = ZMQ.context (1);
        ZMQ.Socket s = ctx.socket (ZMQ.PULL);

        //  Add your socket options here.
        //  For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.

        s.bind ("tcp://localhost:9998");

        byte [] data = s.recv (0);
        assert (data.length == messageSize);

        long start = System.currentTimeMillis ();

        for (int i = 1; i != messageCount; i ++) {
            data = s.recv (0);
            assert (data.length == messageSize);
        }

        long end = System.currentTimeMillis ();

        long elapsed = (end - start) * 1000;
        if (elapsed == 0)
            elapsed = 1;

        long throughput = messageCount * 1000000 / elapsed;
        double megabits = (double) (throughput * messageSize * 8) / 1000000;

        System.out.println ("message size: " + messageSize + " [B]");
        System.out.println ("message count: " + messageCount);
        System.out.println ("mean throughput: " + throughput + "[msg/s]");
        System.out.println ("mean throughput: " + megabits + "[Mb/s]");
    }
}
