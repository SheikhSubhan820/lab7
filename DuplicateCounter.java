package task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DuplicateCounter {

    /**
     * Method to calculate the number of unique duplicate values in an array.
     * @param numbers Input array of integers.
     * @return Count of unique numbers with more than one occurrence.
     */
    public static int countUniqueDuplicates(int[] numbers) {
        // Step 1: Count occurrences of each number
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        // Step 2: Count numbers with frequency > 1
        int duplicateCount = 0;
        for (int count : frequencyMap.values()) {
            if (count > 1) {
                duplicateCount++;
            }
        }
        return duplicateCount;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for input
        System.out.println("Enter the size of the array:");
        int size = scanner.nextInt();
        if (size <= 0) {
            System.out.println("Array size must be greater than 0.");
            return;
        }

        int[] numbers = new int[size];
        System.out.println("Enter " + size + " integers:");
        for (int i = 0; i < size; i++) {
            numbers[i] = scanner.nextInt();
        }

        // Call the method and display the result
        int duplicateCount = countUniqueDuplicates(numbers);

        // Handle edge cases
        if (duplicateCount == 0) {
            System.out.println("No duplicate values found.");
        } else {
            System.out.println("Number of unique duplicate values: " + duplicateCount);
        }

        scanner.close();
    }
}
