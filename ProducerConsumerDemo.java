package task1;

import java.util.LinkedList;

class Buffer {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int CAPACITY = 5; // Maximum size of the buffer

    // Producer method to add items to the buffer
    public synchronized void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            // Wait if the buffer is full
            while (buffer.size() == CAPACITY) {
                wait();
            }

            // Add a new item to the buffer
            buffer.add(value);
            System.out.println("Produced: " + value);
            value++;

            // Notify the consumer
            notify();

            // Simulate delay for producing
            Thread.sleep(500);
        }
    }

    // Consumer method to remove items from the buffer
    public synchronized void consume() throws InterruptedException {
        while (true) {
            // Wait if the buffer is empty
            while (buffer.isEmpty()) {
                wait();
            }

            // Remove an item from the buffer
            int value = buffer.removeFirst();
            System.out.println("Consumed: " + value);

            // Notify the producer
            notify();

            // Simulate delay for consuming
            Thread.sleep(500);
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        // Shared buffer between producer and consumer
        Buffer buffer = new Buffer();

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                buffer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        });

        // Start both threads
        producer.start();
        consumer.start();
    }
}
