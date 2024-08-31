
import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    // Stores the minimum number of moves to the solution
    int moves;
    // Stores the solution as a linked stack of boards
    LinkedStack<Board> solution;

    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        // Corner case: if board is null
        if (board == null) {
            // Throw an error
            throw new NullPointerException("board is null");
        }

        // Corner case: if board is not solvable
        if (!board.isSolvable()) {
            // Throw an error
            throw new IllegalArgumentException("board is unsolvable");
        }

        // Initialize the number of moves to 0
        this.moves = 0;
        // Create a stack to store the solution
        this.solution = new LinkedStack<Board>();
        // Create a priority queue to store the search nodes
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        // Create a search node for the initial board
        SearchNode initialSearchNode = new SearchNode(board, this.moves, null);
        // Add the initial search node to the priority queue
        pq.insert(initialSearchNode);
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            // Check if the current search node has reached the goal
            if (node.board.isGoal()) {
                // If so, add the board to the solution stack
                SearchNode currentMove = node;
                while (currentMove.previous != null) {
                    this.solution.push(currentMove.board);
                    currentMove = currentMove.previous;
                    this.moves++;
                }
                // Finish searching the tree
                break;
            }
            // Otherwise, add the neighboring moves to the priority queue
            for (Board neighbor: node.board.neighbors()) {
                // Check if the current node is the first node
                if (node.previous != null) {
                    // If not, check if the neighbor is the same as the previous board
                    // This will prevent the algorithm from going back and forth between two boards
                    if (neighbor.equals(node.previous.board)) {
                        continue;
                    }
                }
                // Create a new search node for a neighbor, incrementing the number of moves by 1
                SearchNode neighboringMove = new SearchNode(neighbor, moves + 1, node);
                // Add the neighboring node to the priority queue
                pq.insert(neighboringMove);
            }
        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return this.moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return this.solution;
    }

    // A data type that represents a search node in the grame tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        // Stores the board in the node.
        Board board;
        // Stores the number of moves to the node from the initial node.
        int moves;
        // Stores a reference to the previous node.
        SearchNode previous;

        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        //   Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            /* The true manhattan distance is the sum of the
            manhattan distance of the board and the number of moves */
            int thisSum = this.board.manhattan() + this.moves;
            int otherSum = other.board.manhattan() + other.moves;
            return thisSum - otherSum;
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
