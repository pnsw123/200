import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

public class UFPercolation implements Percolation {
    int n;  // Percolation System Size
    int openSites;  // Number of Open Sites
    boolean[][] open;  // Percolation System
    WeightedQuickUnionUF uf;  // Union-find representation of Percolation System

    public UFPercolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        this.n = n;
        open = new boolean[n][n];
        openSites = 0;
        uf = new WeightedQuickUnionUF(n * n + 1);

        // Only connect top row to virtual top site
        for (int j = 0; j < n; j++) {
            uf.union(encode(0, j), 0);
        }
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // Only connect bottom row to virtual bottom site
        if (!open[i][j]) {
            open[i][j] = true;
            openSites++;
            // Connect to virtual top site
            if (i < n - 1 && isOpen(i + 1, j)) {
                uf.union(encode(i + 1, j), encode(i, j));
            }
            if (i > 0 && isOpen(i - 1, j)) {
                uf.union(encode(i - 1, j), encode(i, j));
            }
            if (j < n - 1 && isOpen(i, j + 1)) {
                uf.union(encode(i, j + 1), encode(i, j));
            }
            if (j > 0 && isOpen(i, j - 1)) {
                uf.union(encode(i, j - 1), encode(i, j));
            }
        }
    }
    // Returns true if site (i, j) is open, and false otherwise.

    public boolean isOpen(int i, int j) {
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
        return isOpen(i, j) && uf.connected(0, encode(i, j));
    }
    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }
    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        for (int j = 0; j < n; j++) {
            if (isFull(n - 1, j)) {
                return true;
            }
        }
        return false;
    }
    // Returns the integer corresponding to the site (i, j).
    private int encode(int i, int j) {
        return n * i + j + 1;
    }
    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
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
