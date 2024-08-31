import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    // The underlying BST.
    Node root;
    // The number of nodes in the BST.
    int n;

    // Constructs an empty symbol table.
    public KdTreePointST() {
        // Initialize the root node to null
        this.root = null;
        // Initialize the number of nodes to 0
        this.n = 0;
    }

    // Returns true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return this.n == 0;
    }

    // Returns the number of key-value pairs in this symbol table.
    public int size() {
        return this.n;
    }

    // Inserts the given point and value into this symbol table.
    public void put(Point2D p, Value value) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }
        // Corner case: if value is null
        if (value == null) {
            // Throw an error
            throw new NullPointerException("value is null");
        }
        // The root node has a rectangle of infinite bounds/size
        // The axis of comparison starts along the x-axis
        // The root node starts as null
        this.root = this.put(this.root,
                p, value,
                new RectHV(Double.NEGATIVE_INFINITY,
                        Double.NEGATIVE_INFINITY,
                        Double.POSITIVE_INFINITY,
                        Double.POSITIVE_INFINITY),
                true);
        // Update the size of the BST
        this.n++;
    }

    // Returns the value associated with the given point in this symbol table, or null.
    public Value get(Point2D p) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        return this.get(this.root, p, true);
    }

    // Returns true if this symbol table contains the given point, and false otherwise.
    public boolean contains(Point2D p) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        return this.get(p) != null;
    }

    // Returns all the points in this symbol table.
    public Iterable<Point2D> points() {
        // Create a queue to store the points of the BST
        LinkedQueue<Point2D> pointQueue = new LinkedQueue<Point2D>();
        // Create a second queue to store the nodes of the BST
        LinkedQueue<Node> nodeQueue = new LinkedQueue<Node>();
        // Enqueue the root node
        nodeQueue.enqueue(this.root);
        // Repeat until the node queue has been exausted
        while (!nodeQueue.isEmpty()) {
            // Iterate through the nodes in the node queue
            for (Node currentNode : nodeQueue) {
                /*
                Dequeue from the node queue and enqueue
                the return value's point to the point queue
                */
                pointQueue.enqueue(nodeQueue.dequeue().p);
                /*
                Enqueue the left and right nodes of the
                current node if they contain a node
                */
                if (currentNode.lb != null) {
                    nodeQueue.enqueue(currentNode.lb);
                }
                if (currentNode.rt != null) {
                    nodeQueue.enqueue(currentNode.rt);
                }
            }
        }
        return pointQueue;
    }

    // Returns all the points in this symbol table that are inside the given rectangle.
    public Iterable<Point2D> range(RectHV rect) {
        // Corner case: if rect is null
        if (rect == null) {
            // Throw an error
            throw new NullPointerException("rect is null");
        }

        // Create a queue to store the points of the BST
        LinkedQueue<Point2D> nodeQueue = new LinkedQueue<Point2D>();
        // Make a call to the range private helper method
        this.range(this.root, rect, nodeQueue);
        return nodeQueue;
    }

    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        // Create a variable to store the nearest point, set to null
        Point2D nearestPoint = null;
        // Make a call to the nearest private helper method, returning the output
        return this.nearest(this.root, p, nearestPoint, true);
    }

    // Returns up to k points from this symbol table that are different from and closest to the
    // given point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        // Corner case: if p is null
        if (p == null) {
            // Throw an error
            throw new NullPointerException("p is null");
        }

        // Create a MaxPQ to store the nearest points, comparing them by their distance to p
        MaxPQ<Point2D> nodeMaxPQ = new MaxPQ<Point2D>(p.distanceToOrder());
        // Make a call to the nearest private helper method, returning the output
        this.nearest(this.root, p, k, nodeMaxPQ, true);
        return nodeMaxPQ;
    }

    // Note: In the helper methods that have lr as a parameter, its value specifies how to
    // compare the point p with the point x.p. If true, the points are compared by their
    // x-coordinates; otherwise, the points are compared by their y-coordinates. If the
    // comparison of the coordinates (x or y) is true, the recursive call is made on x.lb;
    // otherwise, the call is made on x.rt.

    // Inserts the given point and value into the KdTree x having rect as its axis-aligned
    // rectangle, and returns a reference to the modified tree.
    private Node put(Node x, Point2D p, Value value, RectHV rect, boolean lr) {
        // If the BST is empty
        if (x == null) {
            // Create a new node and return it
            return new Node(p, value, rect);
        }
        // If the point already exists in the BST
        if (x.p.equals(p)) {
            // Update the value of the point
            x.value = value;
            // Return the BST
            return x;
        }
        if (lr) {
            // If points are compared by their x-coordinates
            if (p.x() < x.p.x()) {
                // The new rectangle is the same as the old one, except it is bounded by the xMax
                RectHV croppedRectangle = new RectHV(x.rect.xMin(),
                        x.rect.yMin(),
                        x.p.x(),
                        x.rect.yMax());
                // Make a recursive call on the left subtree
                x.lb = this.put(x.lb,
                        p, value,
                        croppedRectangle,
                        !lr);
            } else {
                // The new rectangle is the same as the old one, except it is bounded by the xMin
                RectHV croppedRectangle = new RectHV(x.p.x(),
                        x.rect.yMin(),
                        x.rect.xMax(),
                        x.rect.yMax());
                // Make a recursive call on the right subtree
                x.rt = this.put(x.rt,
                        p, value,
                        croppedRectangle,
                        !lr);
            }
        } else {
            // If points are compared by their y-coordinates
            if (p.y() < x.p.y()) {
                // The new rectangle is the same as the old one, except it is bounded by the yMax
                RectHV croppedRectangle = new RectHV(x.rect.xMin(),
                        x.rect.yMin(),
                        x.rect.xMax(),
                        x.p.y());
                // Make a recursive call on the left subtree
                x.lb = this.put(x.lb,
                        p, value,
                        croppedRectangle,
                        !lr);
            } else {
                // The new rectangle is the same as the old one, except it is bounded by the yMin
                RectHV croppedRectangle = new RectHV(x.rect.xMin(),
                        x.p.y(),
                        x.rect.xMax(),
                        x.rect.yMax());
                // Make a recursive call on the right subtree
                x.rt = this.put(x.rt,
                        p, value,
                        croppedRectangle,
                        !lr);
            }
        }
        return x;
    }

    // Returns the value associated with the given point in the KdTree x, or null.
    private Value get(Node x, Point2D p, boolean lr) {
        // If the BST is empty
        if (x == null) {
            // Return null
            return null;
        }
        // If the point exists in the BST
        if (x.p.equals(p)) {
            // Return the value of the point
            return x.value;
        }
        if (lr) {
            // If points are compared by their x-coordinates
            if (p.x() < x.p.x()) {
                // If the point is less than the point at the root
                // Make a recursive call on the left subtree
                return this.get(x.lb, p, !lr);
            } else {
                // If the point is greater than the point at the root
                // Make a recursive call on the right subtree
                return this.get(x.rt, p, !lr);
            }
        } else {
            // If points are compared by their y-coordinates
            if (p.y() < x.p.y()) {
                // If the point is less than the point at the root
                // Make a recursive call on the left subtree
                return this.get(x.lb, p, !lr);
            } else {
                // If the point is greater than the point at the root
                // Make a recursive call on the right subtree
                return this.get(x.rt, p, !lr);
            }
        }
    }

    // Collects in the given queue all the points in the KdTree x that are inside rect.
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        // If the BST is empty
        if (x == null) {
            return;
        }
        // If the rectangle contains the current point
        if (rect.contains(x.p)) {
            // Add the point to the queue
            q.enqueue(x.p);
        }
        // Pruning method: Range search
        if (rect.intersects(x.rect)) {
            // If the rectangle intersects the rectangle of the current point
            // Make a recursive call on the left subtree
            this.range(x.lb, rect, q);
            // Make a recursive call on the right subtree
            this.range(x.rt, rect, q);
        }
    }

    // Returns the point in the KdTree x that is closest to p, or null; nearest is the closest
    // point discovered so far.
    private Point2D nearest(Node x, Point2D p, Point2D nearest, boolean lr) {
        // Make a local variable to store the current nearest point
        Point2D nearestPoint = nearest;
        // If the current node does not exist
        if (x == null) {
            // Skip the rest of the method and return the current nearest point
            return nearestPoint;
        }
        /*
        If the current node is the first node visited,
        it can be deduced that it is the nearest
        */
        if (nearest == null) {
            // The current node must be the nearest point so far
            // Update the nearest point
            nearestPoint = x.p;
        }
        /*
        If the current node isn't the query point
        and it is closer than the current nearest point
        */
        if (!x.p.equals(p) && x.p.distanceSquaredTo(p) < p.distanceSquaredTo(nearestPoint)) {
            // Update the nearest point
            nearestPoint = x.p;
        }
        // Pruning method: Nearest neighbor search in k-d trees
        if ((lr && p.x() <= x.p.x()) || (!lr && p.y() <= x.p.y())) {
            /*
            If the query point...
            compare x-coordinates: lies left of the current node's x-axis
            compare y-coordinates: lies below the current node's y-axis
            */

            // Make the first recursive call on the left subtree
            if (x.lb != null && x.rect.distanceTo(p) <= p.distanceTo(nearestPoint)) {
                /*
                If the left branch is not null and the distance from the query point to the
                rectangle of the currentt node is less than the distance from the query point
                to the current nearest point
                */

                // Make a recursive call on the left subtree
                nearestPoint = this.nearest(x.lb, p, nearestPoint, !lr);
            }
            // Make the second recursive call on the right subtree
            if (x.rt != null && x.rect.distanceTo(p) <= p.distanceTo(nearestPoint)) {
                /*
                If the right branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the current nearest point
                */

                // Make a recursive call on the right subtree
                nearestPoint = this.nearest(x.rt, p, nearestPoint, !lr);
            }
        } else if ((lr && p.x() > x.p.x()) || (!lr && p.y() > x.p.y())) {
            /*
            If the query point...
            compare x-coordinates: lies right of the current node's y-axis
            compare y-coordinates: lies above the current node's x-axis
            */

            // Make the first recursive call on the right subtree
            if (x.rt != null && x.rect.distanceTo(p) <= p.distanceTo(nearestPoint)) {
                /*
                If the right branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the current nearest point
                */

                // Make a recursive call on the right subtree
                nearestPoint = this.nearest(x.rt, p, nearestPoint, !lr);
            }
            // Make the second recursive call on the left subtree
            if (x.lb != null && x.rect.distanceTo(p) <= p.distanceTo(nearestPoint)) {
                /*
                If the left branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the current nearest point
                */

                // Make a recursive call on the left subtree
                nearestPoint = this.nearest(x.lb, p, nearestPoint, !lr);
            }
        }
        return nearestPoint;
    }

    // Collects in the given max-PQ up to k points from the KdTree x that are different from and
    // closest to p.
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, boolean lr) {
        // If the current node does not exist or the max-PQ is full
        if (x == null || pq.size() > k) {
            return;
        }
        // If the current node is not the query point
        if (!x.p.equals(p)) {
            // Add the current point to the max-PQ
            pq.insert(x.p);
        }
        // If the max-PQ is full
        if (pq.size() > k) {
            // Remove the farthest point from the max-PQ
            pq.delMax();
        }
        // Pruning method: K-nearest neighbor search in k-d trees
        if ((lr && p.x() <= x.p.x()) || (!lr && p.y() <= x.p.y())) {
            /*
            If the query point...
            compare x-coordinates: lies left of the current node's x-axis
            compare y-coordinates: lies below the current node's y-axis
            */

            // Make the first recursive call on the left subtree
            if (x.lb != null && x.rect.distanceTo(p) <= p.distanceTo(pq.max())) {
                /*
                If the left branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the farthest point in the max-PQ
                */

                // Make a recursive call on the left subtree
                this.nearest(x.lb, p, k, pq, !lr);
            }
            // Make the second recursive call on the right subtree
            if (x.rt != null && x.rect.distanceTo(p) <= p.distanceTo(pq.max())) {
                /*
                If the right branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the farthest point in the max-PQ
                */

                // Make a recursive call on the right subtree
                this.nearest(x.rt, p, k, pq, !lr);
            }
        } else if ((lr && p.x() > x.p.x()) || (!lr && p.y() > x.p.y())) {
            /*
            If the query point...
            compare x-coordinates: lies right of the current node's y-axis
            compare y-coordinates: lies above the current node's x-axis
            */

            // Make the first recursive call on the right subtree
            if (x.rt != null && x.rect.distanceTo(p) <= p.distanceTo(pq.max())) {
                /*
                If the right branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the farthest point in the max-PQ
                */

                // Make a recursive call on the right subtree
                this.nearest(x.rt, p, k, pq, !lr);
            }
            // Make the second recursive call on the left subtree
            if (x.lb != null && x.rect.distanceTo(p) <= p.distanceTo(pq.max())) {
                /*
                If the left branch is not null and the distance from the query point to the
                rectangle of the current node is less than the distance from the query point
                to the farthest point in the max-PQ
                */

                // Make a recursive call on the left subtree
                this.nearest(x.lb, p, k, pq, !lr);
            }
        }
    }

    // A representation of node in a KdTree in two dimensions (ie, a 2dTree). Each node stores a
    // 2d point (the key), a value, an axis-aligned rectangle, and references to the left/bottom
    // and right/top subtrees.
    private class Node {
        private Point2D p;   // the point (key)
        private Value value; // the value
        private RectHV rect; // the axis-aligned rectangle
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Constructs a node given the point (key), the associated value, and the
        // corresponding axis-aligned rectangle.
        Node(Point2D p, Value value, RectHV rect) {
            this.p = p;
            this.value = value;
            this.rect = rect;
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<>();
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
