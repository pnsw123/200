import stdlib.StdOut;
import stdlib.StdRandom;

public class Sample {
    // Entry point.
    // Accept lo (int), hi (int), k (int), and mode (String) as command-line arguments
    public static void main(String[] args) {
        int lo = Integer.parseInt(args[0]);
        int hi = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);
        String mode = args[3];

        // Validate mode argument
        if (!mode.equals("+") && !mode.equals("-")) {
            throw new IllegalArgumentException("Illegal mode");
        }

        // Initialize random queue
        ResizingArrayRandomQueue<Integer> q = new ResizingArrayRandomQueue<Integer>();

        // Handle sampling with replacement
        if (mode.equals("+")) {
            for (int i = 0; i < k; i++) {
                // Generate and directly print random number since it's with replacement
                StdOut.println(StdRandom.uniform(lo, hi + 1));
            }
        }
        // Handle sampling without replacement
        else if (mode.equals("-")) {
            // Enqueue all numbers in [lo, hi] to the queue
            for (int i = lo; i <= hi; i++) {
                q.enqueue(i);
            }
            // Dequeue and print k elements
            for (int i = 0; i < k; i++) {
                StdOut.println(q.dequeue());
            }
        }
    }
}