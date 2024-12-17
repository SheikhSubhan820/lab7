package task1;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Buffer class for Producer-Consumer
class Buffer {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int CAPACITY = 5;

    public synchronized void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            // Wait if buffer is full
            while (list.size() == CAPACITY) {
                wait();
            }
            list.add(value);
            System.out.println("Produced: " + value);
            value++;
            notify(); // Notify consumer
            Thread.sleep(500);
        }
    }

    public synchronized void consume() throws InterruptedException {
        while (true) {
            // Wait if buffer is empty
            while (list.isEmpty()) {
                wait();
            }
            int value = list.removeFirst();
            System.out.println("Consumed: " + value);
            notify(); // Notify producer
            Thread.sleep(500);
        }
    }
}

// Multithreading for Calculation
class CalculationThread extends Thread {
    private final int start, end;
    private int sum;

    public CalculationThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
            try {
                Thread.sleep(100); // Simulate delay
            } catch (InterruptedException e) {
                System.out.println("Calculation thread interrupted");
            }
        }
        System.out.println("Sum from " + start + " to " + end + " is: " + sum);
    }
}

// Multithreading for Logging
class LoggingThread extends Thread {
    private final int LOG_COUNT = 5;

    @Override
    public void run() {
        try {
            for (int i = 0; i < LOG_COUNT; i++) {
                System.out.println("Logging data... " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Logging thread interrupted");
        }
    }
}

// Main class
public class MultithreadingAndSynchronizationDemo {

    public static void main(String[] args) {
        // Step 1: Demonstrate Producer-Consumer
        Buffer buffer = new Buffer();

        Thread producer = new Thread(() -> {
            try {
                buffer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        // Step 2: Demonstrate Multithreading with Calculation and Logging
        CalculationThread calcThread = new CalculationThread(1, 10);
        LoggingThread logThread = new LoggingThread();

        calcThread.start();
        logThread.start();

        try {
            calcThread.join(); // Wait for calcThread to finish
            logThread.join(); // Wait for logThread to finish
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        // Step 3: Demonstrate Code Optimization and Good Practices
        System.out.println("\n--- Duplicate Count Calculation ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the size of the array: ");
        int size = scanner.nextInt();

        if (size <= 0) {
            System.out.println("Invalid array size. Exiting program.");
            return;
        }

        int[] numbers = new int[size];
        System.out.println("Enter " + size + " integers:");
        for (int i = 0; i < size; i++) {
            numbers[i] = scanner.nextInt();
        }

        int duplicateCount = calculateDuplicateCount(numbers);
        if (duplicateCount == 0) {
            System.out.println("No duplicate values found.");
        } else {
            System.out.println("Number of unique duplicate values: " + duplicateCount);
        }

        System.out.println("All threads and tasks completed.");
        scanner.close();
    }

    // Optimized method for duplicate count
    public static int calculateDuplicateCount(int[] numbers) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }
        int duplicateCount = 0;
        for (int count : frequencyMap.values()) {
            if (count > 1) {
                duplicateCount++;
            }
        }
        return duplicateCount;
    }
}
