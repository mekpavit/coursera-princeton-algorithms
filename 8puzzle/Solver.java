import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Solver {

  private boolean isSolveable;
  private int moves;
  private Stack<Board> solution;

  public Solver(Board initial) {

    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
    MinPQ<SearchNode> minPQOfTwin = new MinPQ<SearchNode>();
    SearchNode currentSearchNode = new SearchNode(initial, 0, initial.manhattan(), null);
    SearchNode currentSearchNodeOfTwin = new SearchNode(initial.twin(), 0, initial.twin().manhattan(), null);
    minPQ.insert(currentSearchNode);
    minPQOfTwin.insert(currentSearchNodeOfTwin);

    boolean isSolved = false;
    boolean isSolvedOfTwin = false;
    while (true) {
      currentSearchNode = minPQ.delMin();
      Board currentBoard = currentSearchNode.board();
      int currentMove = currentSearchNode.moves();
      SearchNode previousSearchNode = currentSearchNode.previous();

      if (currentBoard.isGoal()) {
        isSolved = true;
        break;
      }
      
      for (Board neighbor: currentBoard.neighbors()) {
        if (previousSearchNode != null && neighbor.equals(previousSearchNode.board())) {
          continue;
        }
        SearchNode neighborSearchNode = new SearchNode(neighbor, currentMove + 1, neighbor.manhattan() + currentMove + 1, currentSearchNode);
        minPQ.insert(neighborSearchNode);
      }

      currentSearchNodeOfTwin = minPQOfTwin.delMin();
      Board currentBoardOfTwin = currentSearchNodeOfTwin.board();
      int currentMoveOfTwin = currentSearchNodeOfTwin.moves();
      SearchNode previousSearchNodeOfTwin = currentSearchNodeOfTwin.previous();

      if (currentBoardOfTwin.isGoal()) {
        isSolvedOfTwin = true;
        break;
      }
      
      for (Board neighbor: currentBoardOfTwin.neighbors()) {
        if (previousSearchNodeOfTwin != null && neighbor.equals(previousSearchNodeOfTwin.board())) {
          continue;
        }
        SearchNode neighborSearchNode = new SearchNode(neighbor, currentMoveOfTwin + 1, neighbor.manhattan() + currentMoveOfTwin + 1, currentSearchNodeOfTwin);
        minPQOfTwin.insert(neighborSearchNode);
      }

    }

    if (isSolved) {
      isSolveable = true;
      moves = currentSearchNode.moves();
      solution = new Stack<Board>();
      while (currentSearchNode != null) {
        solution.push(currentSearchNode.board());
        currentSearchNode = currentSearchNode.previous();
      }
    }

    if (isSolvedOfTwin) {
      isSolveable = false;
      moves = -1;
    }

  }

  private class SearchNode implements Comparable<SearchNode> {

    private Board board;
    private int moves;
    private int priority;
    private SearchNode previous;

    public SearchNode(Board board, int moves, int priority, SearchNode previous) {
      this.board = board;
      this.moves = moves;
      this.priority = priority;
      this.previous = previous;
    }

    public int compareTo(SearchNode that) {
      if (this.priority != that.priority) {
        return this.priority - that.priority;
      } else {
        return this.board.manhattan() - that.board.manhattan();
      }
    }

    public Board board() {
      return this.board;
    }

    public int moves() {
      return this.moves;
    }

    public SearchNode previous() {
      return this.previous;
    }

  }

  public boolean isSolvable() {
    return isSolveable;
  }

  public int moves() {
    return moves;
  }

  public Iterable<Board> solution() {
    return solution;
  }

  public static void main(String[] args) {

    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }

  }

}