import java.util.Iterator;
import java.util.NoSuchElementException;
import stdlib.StdOut;
import stdlib.StdRandom;

public class LinkedDeque<Item> implements Iterable<Item> {
    private Node first, last;  // Pointers to the first and last nodes of the deque.
    private int n;  // Size of the deque.

    // Constructor: Initializes an empty deque.
    public LinkedDeque() {
        n = 0;
    }

    // Checks if the deque is empty.
    public boolean isEmpty() {
        return n == 0;
    }

    // Returns the number of items in the deque.
    public int size() {
        return n;
    }

    // Adds an item to the front of the deque.
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("item is null");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        if (isEmpty())
            last = first;  // When the deque is empty, first and last should point to the new node.
        else
            oldfirst.prev = first;  // Link the old first node's previous pointer to the new first node.
        n++;
    }

    // Adds an item to the back of the deque.
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("item is null");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        if (isEmpty())
            first = last;  // If deque is empty, first and last should point to the new node.
        else
            oldlast.next = last;  // Link the old last node's next pointer to the new last node.
        n++;
    }

    // Retrieves the item at the front without removal.
    public Item peekFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return first.item;
    }

    // Removes and returns the item at the front of the deque.
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty())
            last = null;  // If deque is empty after removal, adjust last.
        else
            first.prev = null;  // Nullify the previous pointer of the new first node.
        return item;
    }

    // Retrieves the item at the back without removal.
    public Item peekLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return last.item;
    }

    // Removes and returns the item at the back of the deque.
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = last.item;
        last = last.prev;
        n--;
        if (isEmpty())
            first = null;  // If deque is empty after removal, adjust first.
        else{
            last.next = null;} // Nullify the next pointer of the new last node.
        return item;
    }

    // Provides an iterator to access items from front to back.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // Returns a string representation of the deque.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // Inner class: iterator for the deque.
    private class DequeIterator implements Iterator<Item> {
        Node current = first;  // The iterator starts from the first item.

        // Returns true if there are more items to iterate, and false otherwise.
        public boolean hasNext() {
            return current != null;
        }

        // Retrieves the next item in the iterator.
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Iterator is empty");
            Item item = current.item;
            current = current.next;  // Move the iterator to the next node.
            return item;
        }
    }

    // Inner class: node of the deque.
    private class Node {
        private Item item;  // The item stored in the node.
        private Node next;  // Pointer to the next node.
        private Node prev;  // Pointer to the previous node.
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its several powers, having " +
                "been originally breathed into a few forms or into one; and that, whilst this " +
                "planet has gone cycling on according to the fixed law of gravity, from so simple" +
                " a beginning endless forms most beautiful and most wonderful have been, and are " +
                "being, evolved. ~ Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        StdOut.println("Filling the deque...");
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.printf("The deque (%d characters): ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        StdOut.println("Emptying the deque...");
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            } else {
                deque.removeLast();
            }
        }
        StdOut.println("deque.isEmpty()? " + deque.isEmpty());
    }
}
