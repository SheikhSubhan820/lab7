package task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class BufferWithReentrantLock {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void produce() {
        int value = 0;
        while (true) {
            lock.lock();
            try {
                while (buffer.size() == capacity) {
                    ThreadLogger.logAction("Producer waiting: Buffer is full");
                    notFull.await();
                }
                buffer.add(value);
                ThreadLogger.logAction("Produced: " + value);
                value++;
                notEmpty.signal();
            } catch (InterruptedException e) {
                ThreadLogger.logAction("Producer interrupted");
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }

    public void consume() {
        while (true) {
            lock.lock();
            try {
                while (buffer.isEmpty()) {
                    ThreadLogger.logAction("Consumer waiting: Buffer is empty");
                    notEmpty.await();
                }
                int value = buffer.removeFirst();
                ThreadLogger.logAction("Consumed: " + value);
                notFull.signal();
            } catch (InterruptedException e) {
                ThreadLogger.logAction("Consumer interrupted");
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }
}



public class ReentrantLockChallengeDemo {

    public static void main(String[] args) {
        BufferWithReentrantLock buffer = new BufferWithReentrantLock();

        Thread producer = new Thread(buffer::produce, "Producer");
        Thread consumer = new Thread(buffer::consume, "Consumer");

        producer.start();
        consumer.start();

        try {
            Thread.sleep(5000); // Run for 5 seconds
        } catch (InterruptedException e) {
            ThreadLogger.logAction("Main thread interrupted");
        }

        producer.interrupt();
        consumer.interrupt();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            ThreadLogger.logAction("Main thread join interrupted");
        }

        ThreadLogger.logAction("All threads completed");
    }
}
