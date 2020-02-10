import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class Board {

  private int[][] ts;
  private int emptyTileRow;
  private int emptyTileCol;
  private int hamming = 0;
  private int manhattan = 0;

  public Board(int[][] tiles) {
    validateTiles(tiles);
    ts = new int[tiles.length][tiles.length];
    for (int row = 0; row < tiles.length; row++) {
      for (int col = 0; col < tiles.length; col++) {
        if (tiles[row][col] < 0) {
          throw new IllegalArgumentException();
        }
        ts[row][col] = tiles[row][col];
        if (tiles[row][col] == 0) {
          emptyTileRow = row;
          emptyTileCol = col;
        }
      }
    }

    for (int row = 0; row < ts.length; row++) {
      for (int col = 0; col < ts.length; col++) {
        if (ts[row][col] == 0) {
          continue;
        }
        int goalTile = goalTile(row, col);
        if (ts[row][col] != goalTile) {
          hamming++;
        }
      }
    }

    for (int row = 0; row < ts.length; row++) {
      for (int col = 0; col < ts.length; col++) {
        if (ts[row][col] == 0) {
          continue;
        }
        manhattan = manhattan + Math.abs(row - goalTileRow(ts[row][col])) + Math.abs(col - goalTileCol(ts[row][col]));
      }
    }

  }

  private void validateTiles(int[][] tiles) {
    for (int[] row: tiles) {
      if (row.length != tiles.length) {
        throw new IllegalArgumentException();
      }
    }
  }

  public String toString() {
    String boardString = String.valueOf(ts.length);
    for (int row = 0; row < ts.length; row++) {
      boardString = boardString + "\n";
      for (int col = 0; col < ts.length; col++) {
        boardString = boardString + "\t" + String.valueOf(ts[row][col]);
      }
    }
    return boardString;
  } 

  public int dimension() {
    return ts.length;
  }

  private int goalTile(int row, int col) {
    int goalTile = ts.length * row + col + 1;
    if (goalTile == ts.length * ts.length) {
      goalTile = 0;
    }
    return goalTile;
  }

  private int goalTileRow(int goalTile) {
    if (goalTile == 0) {
      return ts.length - 1;
    }
    return (goalTile - 1) / ts.length;
  }

  private int goalTileCol(int goalTile) {
    if (goalTile == 0) {
      return ts.length - 1;
    }
    return (goalTile - 1) % ts.length;
  }

  public int hamming() {
    return hamming;
  }

  public int manhattan() {
    return manhattan;
  }

  public boolean isGoal() {
    for (int row = 0; row < ts.length; row++) {
      for (int col = 0; col < ts.length; col++) {
        if (ts[row][col] != goalTile(row, col)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }

    if (y == null) {
      return false;
    }

    if (this.getClass() != y.getClass()) {
      return false;
    }

    Board that = (Board) y;

    if (this.ts.length != that.ts.length) {
      return false;
    }

    for (int row = 0; row < ts.length; row++) {
      for (int col = 0; col < ts.length; col++) {
        if (ts[row][col] != that.ts[row][col]) {
          return false;
        }
      }
    }
    return true;
  }

  public Iterable<Board> neighbors() {

    Queue<Board> neighbors = new Queue<Board>();

    if (emptyTileRow != 0) {
      exchange(emptyTileRow, emptyTileCol, emptyTileRow - 1, emptyTileCol);
      neighbors.enqueue(new Board(ts));
      exchange(emptyTileRow, emptyTileCol, emptyTileRow - 1, emptyTileCol);
    }
    if (emptyTileCol != ts.length - 1) {
      exchange(emptyTileRow, emptyTileCol, emptyTileRow, emptyTileCol + 1);
      neighbors.enqueue(new Board(ts));
      exchange(emptyTileRow, emptyTileCol, emptyTileRow, emptyTileCol + 1);
    }
    if (emptyTileRow != ts.length - 1) {
      exchange(emptyTileRow, emptyTileCol, emptyTileRow + 1, emptyTileCol);
      neighbors.enqueue(new Board(ts));
      exchange(emptyTileRow, emptyTileCol, emptyTileRow + 1, emptyTileCol);
    }
    if (emptyTileCol != 0) {
      exchange(emptyTileRow, emptyTileCol, emptyTileRow, emptyTileCol - 1);
      neighbors.enqueue(new Board(ts));
      exchange(emptyTileRow, emptyTileCol, emptyTileRow, emptyTileCol - 1);
    }
    return neighbors;

  }

  private void exchange(int row1, int col1, int row2, int col2) {
    int temp = ts[row1][col1];
    ts[row1][col1] = ts[row2][col2];
    ts[row2][col2] = temp;
  }

  public Board twin() {
    int rowToSwap = 0;
    if (emptyTileRow == 0) {
      rowToSwap = ts.length - 1;
    }
    if (emptyTileRow == ts.length - 1) {
      rowToSwap = 0;
    }
    exchange(rowToSwap, 0, rowToSwap, 1);
    Board twinBoard = new Board(ts);
    exchange(rowToSwap, 0, rowToSwap, 1);
    return twinBoard;
  }

  public static void main(String[] args) {
    // toString
    StdOut.println("toString");
    int[][] b1Array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    Board b1 = new Board(b1Array);
    StdOut.println(b1.toString());
    int[][] b2Array = {{1, 10, 0, 4}, {5, 2, 6, 12}, {9, 8, 3, 11}, {13, 14, 7, 15}};
    Board b2 = new Board(b2Array);
    StdOut.println(b2.toString());

    // dimension
    StdOut.println("dimension");
    StdOut.printf("%-100s%-2s%n", "dimesion of b1 should return 3", b1.dimension() == 3);
    StdOut.printf("%-100s%-2s%n", "dimesion of b2 should return 4", b2.dimension() == 4);

    // hamming
    StdOut.println("hamming");
    StdOut.printf("%-100s%-2s%n", "hamming of b1 should return 0", b1.hamming() == 0);
    StdOut.printf("%-100s%-2s%n", "hamming of b2 should return 11", b2.hamming() == 10);

    // manhattan
    StdOut.println("manhattan");
    StdOut.printf("%-100s%-2s%n", "manhattan of b1 should return 0", b1.manhattan() == 0);
    StdOut.printf("%-100s%-2s%n", "manhattan of b2 should return 11", b2.manhattan() == 18);

    // isGoal
    StdOut.println("isGoal");
    StdOut.printf("%-100s%-2s%n", "isGoal of b1 should return true", b1.isGoal() == true);
    StdOut.printf("%-100s%-2s%n", "isGoal of b2 should return false", b2.isGoal() == false);

    // equals
    StdOut.println("equals");
    Board b1dup = b1;
    StdOut.printf("%-100s%-2s%n", "equals of b1 and b1dup should return true", b1.equals(b1dup) == true);
    StdOut.printf("%-100s%-2s%n", "equals of b1 and null should return false", b1.equals(null) == false);
    int[] testArray = {1, 2, 3};
    StdOut.printf("%-100s%-2s%n", "equals of b1 and array should return false", b1.equals(testArray) == false);
    StdOut.printf("%-100s%-2s%n", "equals of b1 and b2 should return false", b2.equals(b1) == false);
    Board b3 = new Board(b2Array);
    StdOut.printf("%-100s%-2s%n", "equals of b2 and b3 should return true", b2.equals(b3) == true);

    // neighbors
    StdOut.println("neighbors");
    for (Board nb: b1.neighbors()) {
      StdOut.println(nb);
    }
    for (Board nb: b2.neighbors()) {
      StdOut.println(nb);
    }

    // twin
    StdOut.println("neighbors");
    StdOut.println(b1.twin());
    StdOut.println(b2.twin());
  }

}