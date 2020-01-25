import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param  x the <em>x</em>-coordinate of the point
   * @param  y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point
   * to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point.
   * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
   * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
   * +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical;
   * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
   *
   * @param  that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    
    if (x == that.x && y != that.y) { return Double.POSITIVE_INFINITY; }
    else if (y == that.y && x != that.x) { return 0.0; }
    else if (x == that.x && y == that.y) { return Double.NEGATIVE_INFINITY; }
    else { return (that.y - y) / (double) (that.x - x); }

  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate.
   * Formally, the invoking point (x0, y0) is less than the argument point
   * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
   *
   * @param  that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument
   *         point (x0 = x1 and y0 = y1);
   *         a negative integer if this point is less than the argument
   *         point; and a positive integer if this point is greater than the
   *         argument point
   */
  public int compareTo(Point that) {
    if (y > that.y) { return 1; }
    else if (y < that.y) { return -1; }
    else {
      if (x > that.x) { return 1; }
      else if (x < that.x) { return -1; }
      else { return 0; }
    }
  }

  /**
   * Compares two points by the slope they make with this point.
   * The slope is defined as in the slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return new PointComparatorBySlope(this);
  }

  private class PointComparatorBySlope implements Comparator<Point> {

    Point point;

    public PointComparatorBySlope(Point p) {
      point = p;
    }

    public int compare(Point p1, Point p2) {
      if (point.slopeTo(p1) > point.slopeTo(p2)) { return 1; }
      else if (point.slopeTo(p1) < point.slopeTo(p2)) { return -1; }
      else { return 0; }
    }

  }


  /**
   * Returns a string representation of this point.
   * This method is provide for debugging;
   * your program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  public String toString() {
      /* DO NOT MODIFY */
      return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    // compareTo
    StdOut.println("compareTo");
    Point p1 = new Point(6, 20);
    Point p11 = new Point(6, 17);
    StdOut.printf("%-100s%-1s%n", "compareTo should return 1 when compare (6, 20) with (6, 17): ", p1.compareTo(p11) == 1);
    Point p12 = new Point(3, 20);
    StdOut.printf("%-100s%-1s%n", "compareTo should return 1 when compare (6, 20) with (3, 20): ", p1.compareTo(p12) == 1);
    Point p13 = new Point(3, 23);
    StdOut.printf("%-100s%-1s%n", "compareTo should return -1 when compare (6, 20) with (3, 23): ", p1.compareTo(p13) == -1);
    Point p14 = new Point(7, 20);
    StdOut.printf("%-100s%-1s%n", "compareTo should return -1 when compare (6, 20) with (7, 20): ", p1.compareTo(p14) == -1);
    Point p15 = new Point(6, 20);
    StdOut.printf("%-100s%-1s%n", "compareTo should return 0 when compare (6, 20) with (6, 20): ", p1.compareTo(p15) == 0);

    // slopeTo
    StdOut.println("slopeTo");
    StdOut.printf("%-100s%-1s%n", "slopeTo should return positive infinity when input are (6, 20) with (6, 17): ", p1.slopeTo(p11) == Double.POSITIVE_INFINITY);
    StdOut.printf("%-100s%-1s%n", "slopeTo should return 0 when input are (6, 20) with (3, 20): ", p1.slopeTo(p12) == 0.0);
    StdOut.printf("%-100s%-1s%n", "slopeTo should return -1.0 when input are (6, 20) with (3, 23): ", p1.slopeTo(p13) == -1.0);
    StdOut.printf("%-100s%-1s%n", "slopeTo should return 0 when input are (6, 20) with (7, 20): ", p1.slopeTo(p14) == 0.0);
    StdOut.printf("%-100s%-1s%n", "slopeTo should return negative infinity when input are (6, 20) with (6, 20): ", p1.slopeTo(p15) == Double.NEGATIVE_INFINITY);
    Point p16 = new Point(10, 26);
    StdOut.printf("%-100s%-1s%n", "slopeTo should return 1.5 when input are (6, 20) with (10, 26): ", p1.slopeTo(p16) == 1.5);

    //slopeOrder
    Comparator<Point> p1SlopeComparator = p1.slopeOrder();
    StdOut.println("slopeOrder");
    StdOut.printf("%-100s%-1s%n", "slopeComparator.compare of (6, 20) should return positive infinity when input are (6, 20) with (6, 17): ", p1SlopeComparator.compare(p1, p11) < 0);
    StdOut.printf("%-100s%-1s%n", "slopeComparator.compare of (6, 20) should return positive infinity when input are (6, 17) with (3, 20): ", p1SlopeComparator.compare(p11, p12) > 0);
    StdOut.printf("%-100s%-1s%n", "slopeComparator.compare of (6, 20) should return positive infinity when input are (3, 23) with (6, 20): ", p1SlopeComparator.compare(p13, p15) > 0);
    StdOut.printf("%-100s%-1s%n", "slopeComparator.compare of (6, 20) should return positive infinity when input are (6, 20) with (6, 20): ", p1SlopeComparator.compare(p15, p15) == 0);
  }

}