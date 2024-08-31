import java.util.Iterator;
import java.util.NoSuchElementException;
import stdlib.StdOut;
import stdlib.StdRandom;

// A data type to represent a random queue, implemented using a resizing array as the underlying
// data structure.
public class ResizingArrayRandomQueue<Item> implements Iterable<Item> {
    private Item[] q = (Item[]) new Object[2];
    // size of queue
    private int n;

    // Constructs an empty random queue.
    public ResizingArrayRandomQueue() {
        n = 0;
    }
    // Returns true if this queue is empty, and false otherwise.
    public boolean isEmpty() {
        if (n == 0) {
            return true;
        }
        return false;
    }

    // Returns the number of items in this queue.
    public int size() {
        return n;
    }

    // Adds item to the end of this queue.
    public void enqueue(Item item)  {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        if (q.length == n) {
            resize(2 * q.length);
        }
        q[n] = item;
        n++;
    }

    // Returns a random item from this queue.
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Random queue is empty");
        return q[StdRandom.uniform(n)];
    }

    // Removes and returns a random item from this queue.
    // Removes and returns a random item from this queue.
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Random queue is empty");

        int r = StdRandom.uniform(n);
        Item item = q[r]; // Store the item to be returned.
        q[r] = q[n - 1];  // Swap the item to be removed with the last item.
        q[n - 1] = null;  // Nullify the last item to prevent loitering.

        if (n <= q.length / 4) {
            resize(q.length / 2);
        }
        n--;

        return item; // Return the stored item.
    }

    // Returns an independent iterator to iterate over the items in this queue in random order.
    public Iterator<Item> iterator() {
        RandomQueueIterator iterator = new RandomQueueIterator();
        return iterator;
    }

    // Returns a string representation of this queue.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // An iterator, doesn't implement remove() since it's optional.
    private class RandomQueueIterator implements Iterator<Item> {
            private Item[] items = (Item[]) new Object[n]; // Assuming items is the array for shuffling
            private int current = 0; // Assuming current is the index of the next item to return

            // Constructs an iterator.
            public RandomQueueIterator() {
                for (int j = 0; j < n; j++) {
                    items[j] = q[j];
                }
                StdRandom.shuffle(items);
            }

            // Returns true if there are more items to iterate, and false otherwise.
            public boolean hasNext() {
                return current < n;
            }

            // Returns the next item.
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException("Iterator is empty");
                return items[current++];
            }
        }

    // Resizes the underlying array.
    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            if (q[i] != null) {
                temp[i] = q[i];
            }
        }
        q = temp;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        ResizingArrayRandomQueue<Integer> q = new ResizingArrayRandomQueue<Integer>();
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            int r = StdRandom.uniform(10000);
            q.enqueue(r);
            sum += r;
        }
        int iterSumQ = 0;
        for (int x : q) {
            iterSumQ += x;
        }
        int dequeSumQ = 0;
        while (q.size() > 0) {
            dequeSumQ += q.dequeue();
        }
        StdOut.println("sum       = " + sum);
        StdOut.println("iterSumQ  = " + iterSumQ);
        StdOut.println("dequeSumQ = " + dequeSumQ);
        StdOut.println("iterSumQ + dequeSumQ == 2 * sum? " + (iterSumQ + dequeSumQ == 2 * sum));
    }
}