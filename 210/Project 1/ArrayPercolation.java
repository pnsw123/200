import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {
    int n; // n-by-n percolation system
    boolean[][] open; // open[i][j] = is site (i, j) open?
    int openSites; // number of open sites
    boolean[][] full; // full[i][j] = is site (i, j) full?


    // Constructs an n x n percolation system, with all sites blocked.
    public ArrayPercolation(int n) {
        // ArrayPercolation() should throw an IllegalArgumentException("Illegal n") if n ≤ 0.
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        this.n = n;
        open = new boolean[n][n];
        full = new boolean[n][n];
        openSites = 0;
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        if (!isOpen(i, j)) {
            open[i][j] = true;
            openSites++;}
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
    //should throw an IndexOutOfBoundsException("Illegal i or j") if i or j is outside the interval [0, n−1].
    if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
    return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // Create a new 'full' array for each isFull query.
        full = new boolean[n][n];
        // Call floodFill on every site in the first row.
        for (int col = 0; col < n; col++) {
            if (isOpen(0, col)) {
                floodFill(full, 0, col);
            }
        }
        // Return 'full[i][j]'.
        return full[i][j];
    }


    // Returns the number of open sites.
    public int numberOfOpenSites() {
        //should run in time T(n) ∼ 1.
    return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // Create a separate boolean array to track fullness for percolation checking.
    boolean[][] connected = new boolean[n][n];
    // Check for percolation starting from each site in the top row.
    for (int j = 0; j < n; j++) {
        if (!connected[0][j] && isOpen(0, j)) {
            floodFill(connected, 0, j);
        }
    }
    // Check if any site in the bottom row is connected to the top row.
    for (int j = 0; j < n; j++) {
        if (connected[n - 1][j]) {
            return true;
        }
    }
    return false;
    }
     // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    private void floodFill(boolean[][] full, int i, int j) {
        if (i < 0 || i >= n) return; // outside grid
        if (j < 0 || j >= n) return; // outside grid
        if (!isOpen(i, j)) return; // not an open site
        if (full[i][j]) return; // already marked as full
        full[i][j] = true;
        floodFill(full, i + 1, j);
        floodFill(full, i - 1, j);
        floodFill(full, i, j + 1);
        floodFill(full, i, j - 1);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }

}
