package edu.cmu.cs.cs214.rec08.queue;

import java.util.ArrayDeque;
import java.util.Deque;

import net.jcip.annotations.ThreadSafe;
import net.jcip.annotations.GuardedBy;

/**
 * An UnboundedBlockingQueue implementation that is thread-safe.
 * Enqueuing an element always succeeds immediately.
 * Dequeuing from an empty queue blocks until an element is enqueued by another thread.
 */
@ThreadSafe
public class UnboundedBlockingQueue<E> implements SimpleQueue<E> {
    @GuardedBy("this")
    private final Deque<E> queue = new ArrayDeque<>();

    public UnboundedBlockingQueue() { }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized E peek() {
        return queue.peek();
    }

    /**
     * Enqueues the specified element into the queue.
     * This method always succeeds immediately.
     *
     * @param element the element to add
     */
    public synchronized void enqueue(E element) {
        queue.add(element);
        notify(); // Notify a waiting thread that an element has been added
    }

    /**
     * Dequeues an element from the queue.
     * If the queue is empty, this method blocks until an element is available.
     *
     * @return the dequeued element
     */
    public synchronized E dequeue() {
        while (queue.isEmpty()) {
            try {
                wait(); // Wait until an element is enqueued
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                // Optionally, you can throw an exception or return null here
                return null;
            }
        }
        return queue.remove();
    }
}
