import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;


public class PointSET {

  private SET<Point2D> bst;

  public PointSET() {
    this.bst = new SET<Point2D>();
  }

  private void validateArgument(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
  }

  public boolean isEmpty() {
    return this.bst.isEmpty();
  }

  public int size() {
    return this.bst.size();
  }

  public void insert(Point2D p) {
    this.validateArgument(p);
    this.bst.add(p);
  }

  public boolean contains(Point2D p) {
    this.validateArgument(p);
    return this.bst.contains(p);
  }

  public void draw() {
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    for (Point2D p: this.bst) {
      p.draw();
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    this.validateArgument(rect);
    ArrayList<Point2D> pointsInsideRect = new ArrayList<Point2D>();
    for (Point2D p: this.bst) {
      if (rect.contains(p)) {
        pointsInsideRect.add(p);
      }
    }
    return pointsInsideRect;
  }

  public Point2D nearest(Point2D p) {
    this.validateArgument(p);
    double nearestDistance = Double.MAX_VALUE;
    Point2D nearestPoint = null;
    for (Point2D pointInSET: this.bst) {
      double distance = p.distanceSquaredTo(pointInSET);
      if (distance < nearestDistance) {
        nearestDistance = distance;
        nearestPoint = pointInSET;
      }
    }
    return nearestPoint;
  }

  public static void main(String[] args) {

    // isEmpty
    PointSET testPSET1 = new PointSET();
    StdOut.println("isEmpty");
    StdOut.printf("%-100s%-2s%n", "when PointSET is empty, it should return true", testPSET1.isEmpty() == true);
    testPSET1.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET is not empty, it should return false", testPSET1.isEmpty() == false);

    // size
    PointSET testPSET2 = new PointSET();
    StdOut.println("size");
    StdOut.printf("%-100s%-2s%n", "when PointSET is empty, it should return 0", testPSET2.size() == 0);
    testPSET2.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET has 1 point, it should return 1", testPSET2.size() == 1);
    testPSET2.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET has 2 point, it should return 2", testPSET2.size() == 2);
    testPSET2.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET has 2 point and duplicated point is inserted, it should still return 2", testPSET2.size() == 2);

    // contains
    PointSET testPSET3 = new PointSET();
    StdOut.println("contains");
    StdOut.printf("%-100s%-2s%n", "when PointSET is empty, it should return false", testPSET3.contains(new Point2D(2, 2)) == false);
    testPSET3.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET has (1, 1), it should return true", testPSET3.contains(new Point2D(1, 1)) == true);
    testPSET3.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when PointSET has (2, 2), it should return true", testPSET3.contains(new Point2D(2, 1)) == true);
    StdOut.printf("%-100s%-2s%n", "when PointSET does not have (3, 2), it should return false", testPSET3.contains(new Point2D(3, 2)) == false);

    // draw
    PointSET testPSET4 = new PointSET();
    testPSET4.insert(new Point2D(0.5, 0.5));
    testPSET4.insert(new Point2D(0.2, 0.5));
    testPSET4.insert(new Point2D(0.1, 0.1));
    testPSET4.draw();

    // range
    StdOut.println("range");
    PointSET testPSET5 = new PointSET();
    testPSET5.insert(new Point2D(0.1, 0.1));
    testPSET5.insert(new Point2D(0.3, 0.1));
    testPSET5.insert(new Point2D(0.2, 0.2));
    testPSET5.insert(new Point2D(0.2, 0.3));
    testPSET5.insert(new Point2D(0.5, 0.5));
    testPSET5.insert(new Point2D(0.8, 0.2));
    RectHV testRect = new RectHV(0, 0, 0.4, 0.4);
    ArrayList<Point2D> pointsInsideTestRect = (ArrayList<Point2D>) testPSET5.range(testRect);
    StdOut.printf("%-100s%-2s%n", "(0.1, 0.1) should be in range", pointsInsideTestRect.contains(new Point2D(0.1, 0.1)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.3, 0.1) should be in range", pointsInsideTestRect.contains(new Point2D(0.3, 0.1)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.2, 0.2) should be in range", pointsInsideTestRect.contains(new Point2D(0.2, 0.2)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.2, 0.3) should be in range", pointsInsideTestRect.contains(new Point2D(0.2, 0.3)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.5, 0.5) should not be in range", pointsInsideTestRect.contains(new Point2D(0.5, 0.5)) == false);
    StdOut.printf("%-100s%-2s%n", "(0.8, 0.2) should not be in range", pointsInsideTestRect.contains(new Point2D(0.8, 0.2)) == false);

    // nearest
    StdOut.println("range");
    StdOut.printf("%-100s%-2s%n", "the nearest point to (0.19, 0.24) should be (0.2, 0.2)", testPSET5.nearest(new Point2D(0.19, 0.24)).equals(new Point2D(0.2, 0.2)));
    StdOut.printf("%-100s%-2s%n", "the nearest point to (0.9, 0.9) should be (0.5, 0.5)", testPSET5.nearest(new Point2D(0.9, 0.9)).equals(new Point2D(0.5, 0.5)));
  }

}