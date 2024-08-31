
import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// Represents an 8-puzzle board or its generalizations.
public class Board {
    int[][] tiles; // Board tiles
    int n; // Board size
    int hamming; // Hamming priority value
    int manhattan; // Manhattan priority value
    int blankPos; // Blank tile position in row-major order

    // Constructor initializes board from n x n array, 0 denotes blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        this.hamming = 0;
        this.manhattan = 0;
        this.blankPos = 0;

        // Calculate hamming and manhattan distances
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int goalValue = i * this.n + j + 1;
                int tileValue = this.tiles[i][j];
                if (tileValue == 0) {
                    this.blankPos = i * this.n + j + 1; // Correct position of the blank tile
                    continue;
                }
                if (tileValue != goalValue) this.hamming++;

                // Corrected Manhattan distance calculation
                int goalI = (tileValue - 1) / this.n;
                int goalJ = (tileValue - 1) % this.n;
                this.manhattan += Math.abs(goalI - i) + Math.abs(goalJ - j);
            }
        }
    }

    // Returns board size.
    public int size() { return this.n; }

    // Returns tile at (i, j).
    public int tileAt(int i, int j) { return this.tiles[i][j]; }

    // Returns Hamming distance to goal.
    public int hamming() { return this.hamming; }

    // Returns Manhattan distance to goal.
    public int manhattan() { return this.manhattan; }

    // Checks if current board is goal.
    public boolean isGoal() { return this.hamming == 0; }

    // Checks if board is solvable.
    public boolean isSolvable() {
        int[] rowMajorBoard = new int[this.n * this.n - 1];
        int count = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int currentRowMajor = this.tileAt(i, j);
                if (currentRowMajor != 0) rowMajorBoard[count++] = currentRowMajor;
            }
        }
        int inversionCount = (int) Inversions.count(rowMajorBoard);
        if (this.n * this.n % 2 == 1) {
            return inversionCount % 2 == 0;
        } else {
            int blankRow = (this.blankPos - 1) / this.n;
            return (inversionCount + blankRow) % 2 == 1;
        }
    }

    // Returns neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int blankRow = (this.blankPos - 1) / this.n;
        int blankCol = (this.blankPos - 1) % this.n;
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        for (int[] i : directions) {
            int row = blankRow + i[0];
            int col = blankCol + i[1];
            if (row >= 0 && row < this.n && col >= 0 && col < this.n) {
                int[][] tilesCopy = this.cloneTiles();
                tilesCopy[blankRow][blankCol] = this.tileAt(row, col);
                tilesCopy[row][col] = this.tileAt(blankRow, blankCol);
                q.enqueue(new Board(tilesCopy));
            }
        }
        return q;
    }

    // Checks board equality.
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass()) return false;
        if (other == this) return true;
        Board that = (Board) other;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // String representation of the board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) s.append(" ");
            }
            if (i < n - 1) s.append("\n");
        }
        return s.toString();
    }

    // Creates a copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(tiles[i], 0, clone[i], 0, n);
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
