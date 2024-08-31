import dsa.LinkedQueue;
import dsa.MinPQ;
import dsa.Point2D;
import dsa.RectHV;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.StdIn;
import stdlib.StdOut;

// A symbol table of points implemented using a red-black BST.
public class BrutePointST<Value> implements PointST<Value> {
    // Instance variable.
    RedBlackBinarySearchTreeST<Point2D, Value> bst;

    // Constructs an empty symbol table of points.
    public BrutePointST() {
        bst = new RedBlackBinarySearchTreeST<Point2D, Value>();
    }

   // true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return this.bst.isEmpty();
    }

    // return the key value pairs in the symbol table
    public int size() {
        return this.bst.size();
    }

    // given point p, insert a value into the symbol table
    public void put(Point2D p, Value value) {
        // corner case. if p is null, throw an error
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }
        // Corner case: if value is null
        if (value == null) {
            // Throw an error
            throw new NullPointerException("value is null");
        }

        this.bst.put(p, value);
    }

    // return every value associated with the given point in this symbol table, or null
    public Value get(Point2D p) {
        // corenr case. if p is null, throw an error
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        return this.bst.get(p);
    }

    // return true if this table contains the given point, and false otherwise
    public boolean contains(Point2D p) {

    // corner case, if p is null, throw an error
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }
    // return true if this table contains the given point, and false otherwise
        return this.bst.contains(p);
    }

    // Returns all the points in this symbol table.
    public Iterable<Point2D> points() {
    // return all the points in this symbol table
        return this.bst.keys();
    }

  // return every point in this symbol table that is inside the given rectangle
    public Iterable<Point2D> range(RectHV rect) {
        // Corner case: if rect is null
        if (rect == null) {
            // Throw an error
            throw new NullPointerException("rect is null");
        }

        // iniiate a queue to store the points in the BST
        LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
        // iterate over the points in the BST
        for (Point2D point : this.bst.keys()) {
            // If the point is in the rectangle, add it to the queue
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }
        // return the queue
        return queue;
    }

    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        // Create a priority queue to store the points in the BST
        MinPQ<Point2D> pq = new MinPQ<Point2D>(p.distanceToOrder());
        // Iterate through the points in the BST
        for (Point2D point : this.bst.keys()) {
            // If the point is not the same as the given point, add it to the priority queue
            if (!point.equals(p)) {
                pq.insert(point);
            }
        }
        return pq.delMin();
    }

    // Retrieves a maximum of k points from this symbol table, each being distinct from and nearest to the specified point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        // Create a priority queue to store the points in the BST
        // Use the distanceToOrder() comparator method to compare the points
        MinPQ<Point2D> pq = new MinPQ<Point2D>(p.distanceToOrder());
        // Iterate through the points in the BST
        for (Point2D point : this.bst.keys()) {
            // Add the point to the priority queue if it differs from the specified point.
            if (!point.equals(p)) {
                pq.insert(point);
            }
        }
        // Initialize a queue to hold the k nearest points.
        LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
        // Iterate through the priority queue and add the k closest points to the queue
        for (int i = 0; i < k; i++) {
            queue.enqueue(pq.delMin());
        }
        return queue;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        BrutePointST<Integer> st = new BrutePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        int k = Integer.parseInt(args[2]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(-1, -1, 1, 1);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.printf("st.contains(%s)? %s\n", query, st.contains(query));
        StdOut.printf("st.range(%s):\n", rect);
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.printf("st.nearest(%s) = %s\n", query, st.nearest(query));
        StdOut.printf("st.nearest(%s, %d):\n", query, k);
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
