import dsa.LinkedStack;
import stdlib.StdIn;
import stdlib.StdOut;

public class Sort {
    public static void main(String[] args) {
        // Create a deque and a stack.
        LinkedDeque<String> d = new LinkedDeque<String>();
        LinkedStack<String> s = new LinkedStack<String>();
        // Read words from standard input and add them to the deque.
        while (!StdIn.isEmpty()) {
            String w = StdIn.readString();
            if (d.isEmpty()) {
                d.addFirst(w);
            } else {
                // if w is < the first word in d, add w to the front of d.
                if (less(w, d.peekFirst())) {
                    d.addFirst(w);
                }
                // If w is > the last word in d, add w to the back of d.
                else if (less(d.peekLast(), w)) {
                    d.addLast(w);
                }
                // else, delete words less than w from the front of d, store them in s,
                // add w to the front of d, and re-add words from s to the front of d.
                else {
                    while (!d.isEmpty() && less(d.peekFirst(), w)) {
                        s.push(d.removeFirst());
                    }
                    // Add w to the front of d.
                    d.addFirst(w);
                    while (!s.isEmpty()) {
                        d.addFirst(s.pop());
                    }
                }
            }
        }

        // Write the words from d to standard output.
        while (!d.isEmpty()) {
            StdOut.println(d.removeFirst());
        }
    }

    private static boolean less(String v, String w) {
        return v.compareTo(w) < 0;
    }
}
