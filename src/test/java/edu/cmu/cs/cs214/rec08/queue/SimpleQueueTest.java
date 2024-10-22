package edu.cmu.cs.cs214.rec08.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.cmu.cs.cs214.rec08.queue.runnable.Consumer;
import edu.cmu.cs.cs214.rec08.queue.runnable.Producer;
import edu.cmu.cs.cs214.rec08.queue.runnable.SpinWaitConsumer;

public class SimpleQueueTest {
    private static SimpleQueue<Integer> makeEmptyQueue() {
        return new UnboundedBlockingQueue<Integer>();
    }

    @Test
    public void testSize() {
        SimpleQueue<Integer> q = makeEmptyQueue();
        Thread t1 = new Thread(new Producer(q, 100000));
        Thread t2 = new Thread(new Producer(q, 100000));
        Thread t3 = new Thread(new SpinWaitConsumer(q, 200000));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, q.size(), "SimpleQueue size should be zero");
    }

    @Test
    public void testEnqueue() throws Exception {
        final int SIZE = 1000;
        SimpleQueue<Integer> q = makeEmptyQueue();
        Thread t1 = new Thread(new Producer(q, SIZE));
        Thread t2 = new Thread(new Producer(q, SIZE));
        t1.start();  // Inserts two of each item 0..SIZE-1 into the queue.
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueueBlocking() {
        SimpleQueue<Integer> q = new UnboundedBlockingQueue<Integer>();
        Thread t1 = new Thread(new Producer(q, 200000));
        Thread t2 = new Thread(new Consumer(q, 100000));
        Thread t3 = new Thread(new Consumer(q, 100000));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, q.size(), "SimpleQueue size should be zero");
    }
}