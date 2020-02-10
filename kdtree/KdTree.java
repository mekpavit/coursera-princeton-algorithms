import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;


public class KdTree {
    
  private static class Node {
    public Point2D p;
    public RectHV rect;
    public Node lb;
    public Node rt;

    public Node(Point2D p, RectHV rect, Node lb, Node rt) {
      this.p = p;
      this.rect = rect;
      this.lb = lb;
      this.rt = rt;
    }

  }

  private Node root;
  private int size;

  public KdTree() {
    this.root = null;
    this.size = 0;
  }

  private void validateArgument(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
  }

  public boolean isEmpty() {
    return this.root == null;
  }

  public int size() {
    return size;
  }

  public void insert(Point2D p) {
    this.validateArgument(p);
    if (this.contains(p)) {
      return;
    }
    this.root = this.insert(this.root, p, null, true);
  }

  private Node insert(Node n, Point2D p, Node parentNode, boolean checkX) {

    if (n == null) {
      this.size++;
      if (parentNode == null) {
        return new Node(p, new RectHV(0, 0, 1, 1), null, null);
      }
      double cmp;
      if (!checkX) {
        cmp = p.x() - parentNode.p.x();
      } else {
        cmp = p.y() - parentNode.p.y();
      }
      RectHV rect;
      if (cmp < 0) { 
        double childRectXMin = parentNode.rect.xmin();
        double childRectYMin = parentNode.rect.ymin();
        double childRectXMax = (!checkX) ? parentNode.p.x() : parentNode.rect.xmax();
        double childRectYMax = (!checkX) ? parentNode.rect.ymax() : parentNode.p.y();
        rect = new RectHV(childRectXMin, childRectYMin, childRectXMax, childRectYMax);
      } else {
        double childRectXMin = (!checkX) ? parentNode.p.x() : parentNode.rect.xmin();
        double childRectYMin = (!checkX) ? parentNode.rect.ymin() : parentNode.p.y();
        double childRectXMax = parentNode.rect.xmax();
        double childRectYMax = parentNode.rect.ymax();
        rect = new RectHV(childRectXMin, childRectYMin, childRectXMax, childRectYMax);
      }
      return new Node(p, rect, null, null);
    }

    double cmp;
    if (checkX) {
      cmp = p.x() - n.p.x();
    } else {
      cmp = p.y() - n.p.y();
    }
    if (cmp < 0) { 
      n.lb = this.insert(n.lb, p, n, !checkX);
    } else {
      n.rt = this.insert(n.rt, p, n, !checkX);
    }
    return n;
  }

  public boolean contains(Point2D p) {
    this.validateArgument(p);
    return this.contains(this.root, p, true);
  }

  private boolean contains(Node n, Point2D p, boolean checkX) {
    if (n == null) {
      return false;
    }
  
    if (n.p.equals(p)) {
      return true;
    }

    double cmp;
    if (checkX) {
      cmp = p.x() - n.p.x();
    } else {
      cmp = p.y() - n.p.y();
    }
    if (cmp < 0) {
      return this.contains(n.lb, p, !checkX);
    } else {
      return this.contains(n.rt, p, !checkX);
    }

  }

  public void draw() {
    this.draw(this.root, true);
  }

  private void draw(Node n, boolean verticalLine) {
    if (n == null) {
      return;
    }

    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    n.p.draw();

    StdDraw.setPenRadius();
    if (verticalLine) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
    }

    this.draw(n.lb, !verticalLine);
    this.draw(n.rt, !verticalLine);
  }

  public Iterable<Point2D> range(RectHV rect) {
    this.validateArgument(rect);
    ArrayList<Point2D> pointsInsideRect = new ArrayList<Point2D>();
    this.range(this.root, rect, pointsInsideRect);
    return pointsInsideRect;
  }

  private void range(Node n, RectHV rect, ArrayList<Point2D> pointsInsideRect) {
    if (n == null) {
      return;
    }

    if (rect.contains(n.p)) {
      pointsInsideRect.add(n.p);
    }
    if (n.lb != null) {
      if (rect.intersects(n.lb.rect)) {
        this.range(n.lb, rect, pointsInsideRect);
      }
    }
    if (n.rt != null) {
      if (rect.intersects(n.rt.rect)) {
        this.range(n.rt, rect, pointsInsideRect);
      }
    }
  }

  public Point2D nearest(Point2D p) {
    this.validateArgument(p);
    if (this.isEmpty()) {
      return null;
    }
    double distanceFromRoot = p.distanceSquaredTo(this.root.p);
    return this.nearest(this.root, p, this.root.p, distanceFromRoot);
  }

  private Point2D nearest(Node n, Point2D p, Point2D currentNearestPoint, double shortestDistance) {
    double currentNodeDistance = p.distanceSquaredTo(n.p);
    if (currentNodeDistance < shortestDistance) {
      shortestDistance = currentNodeDistance;
      currentNearestPoint = n.p;
    }
    double distanceFromPointToLeftRect = Double.MAX_VALUE;
    double distanceFromPointToRightRect = Double.MAX_VALUE;
    if (n.lb != null) {
      distanceFromPointToLeftRect = n.lb.rect.distanceSquaredTo(p);
    }
    if (n.rt != null) {
      distanceFromPointToRightRect = n.rt.rect.distanceSquaredTo(p);
    }

    if (distanceFromPointToLeftRect < distanceFromPointToRightRect) {
      if (n.lb != null) {
        if (distanceFromPointToLeftRect < shortestDistance) {
          Point2D nearestPointInLeftBottom = this.nearest(n.lb, p, currentNearestPoint, shortestDistance);
          double shortestDistanceFromLeftBottom = p.distanceSquaredTo(nearestPointInLeftBottom);
          if (shortestDistanceFromLeftBottom < shortestDistance) {
            shortestDistance = shortestDistanceFromLeftBottom;
            currentNearestPoint = nearestPointInLeftBottom;
          }
        }
      }
      if (n.rt != null) {
        if (distanceFromPointToRightRect < shortestDistance) {
          Point2D nearestPointInRightTop= this.nearest(n.rt, p, currentNearestPoint, shortestDistance);
          double shortestDistanceFromRightTop = p.distanceSquaredTo(nearestPointInRightTop);
          if (shortestDistanceFromRightTop < shortestDistance) {
            shortestDistance = shortestDistanceFromRightTop;
            currentNearestPoint = nearestPointInRightTop;
          }
        }
      }
    } else {
      if (n.rt != null) {
        if (distanceFromPointToRightRect < shortestDistance) {
          Point2D nearestPointInRightTop= this.nearest(n.rt, p, currentNearestPoint, shortestDistance);
          double shortestDistanceFromRightTop = p.distanceSquaredTo(nearestPointInRightTop);
          if (shortestDistanceFromRightTop < shortestDistance) {
            shortestDistance = shortestDistanceFromRightTop;
            currentNearestPoint = nearestPointInRightTop;
          }
        }
      }
      if (n.lb != null) {
        if (distanceFromPointToLeftRect < shortestDistance) {
          Point2D nearestPointInLeftBottom = this.nearest(n.lb, p, currentNearestPoint, shortestDistance);
          double shortestDistanceFromLeftBottom = p.distanceSquaredTo(nearestPointInLeftBottom);
          if (shortestDistanceFromLeftBottom < shortestDistance) {
            shortestDistance = shortestDistanceFromLeftBottom;
            currentNearestPoint = nearestPointInLeftBottom;
          }
        }
      }
    }

    return currentNearestPoint;
  }

  public static void main(String[] args) {

    // isEmpty
    KdTree testPSET1 = new KdTree();
    StdOut.println("isEmpty");
    StdOut.printf("%-100s%-2s%n", "when KdTree is empty, it should return true", testPSET1.isEmpty() == true);
    testPSET1.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree is not empty, it should return false", testPSET1.isEmpty() == false);

    // size
    KdTree testPSET2 = new KdTree();
    StdOut.println("size");
    StdOut.printf("%-100s%-2s%n", "when KdTree is empty, it should return 0", testPSET2.size() == 0);
    testPSET2.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree has 1 point, it should return 1", testPSET2.size() == 1);
    testPSET2.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree has 2 point, it should return 2", testPSET2.size() == 2);
    testPSET2.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree has 2 point and duplicated point is inserted, it should still return 2", testPSET2.size() == 2);

    // contains
    KdTree testPSET3 = new KdTree();
    StdOut.println("contains");
    StdOut.printf("%-100s%-2s%n", "when KdTree is empty, it should return false", testPSET3.contains(new Point2D(2, 2)) == false);
    testPSET3.insert(new Point2D(1, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree has (1, 1), it should return true", testPSET3.contains(new Point2D(1, 1)) == true);
    testPSET3.insert(new Point2D(2, 1));
    StdOut.printf("%-100s%-2s%n", "when KdTree has (2, 2), it should return true", testPSET3.contains(new Point2D(2, 1)) == true);
    StdOut.printf("%-100s%-2s%n", "when KdTree does not have (3, 2), it should return false", testPSET3.contains(new Point2D(3, 2)) == false);

    // draw
    KdTree testPSET4 = new KdTree();
    testPSET4.insert(new Point2D(0.5, 0.5));
    testPSET4.insert(new Point2D(0.2, 0.5));
    testPSET4.insert(new Point2D(0.1, 0.1));
    testPSET4.draw();

    // range
    StdOut.println("range");
    KdTree testPSET5 = new KdTree();
    testPSET5.insert(new Point2D(0.1, 0.1));
    StdOut.println('a');
    testPSET5.insert(new Point2D(0.3, 0.1));
    StdOut.println('a');
    testPSET5.insert(new Point2D(0.2, 0.2));
    StdOut.println('a');
    testPSET5.insert(new Point2D(0.2, 0.3));
    StdOut.println('a');
    testPSET5.insert(new Point2D(0.5, 0.5));
    StdOut.println('a');
    testPSET5.insert(new Point2D(0.8, 0.2));
    StdOut.println('a');
    RectHV testRect = new RectHV(0, 0, 0.4, 0.4);
    ArrayList<Point2D> pointsInsideTestRect = (ArrayList<Point2D>) testPSET5.range(testRect);
    StdOut.printf("%-100s%-2s%n", "(0.1, 0.1) should be in range", pointsInsideTestRect.contains(new Point2D(0.1, 0.1)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.3, 0.1) should be in range", pointsInsideTestRect.contains(new Point2D(0.3, 0.1)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.2, 0.2) should be in range", pointsInsideTestRect.contains(new Point2D(0.2, 0.2)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.2, 0.3) should be in range", pointsInsideTestRect.contains(new Point2D(0.2, 0.3)) == true);
    StdOut.printf("%-100s%-2s%n", "(0.5, 0.5) should not be in range", pointsInsideTestRect.contains(new Point2D(0.5, 0.5)) == false);
    StdOut.printf("%-100s%-2s%n", "(0.8, 0.2) should not be in range", pointsInsideTestRect.contains(new Point2D(0.8, 0.2)) == false);

    // nearest
    StdOut.println("nearest");
    StdOut.println(testPSET5.nearest(new Point2D(0.19, 0.24)));
    StdOut.printf("%-100s%-2s%n", "the nearest point to (0.19, 0.24) should be (0.2, 0.2)", testPSET5.nearest(new Point2D(0.19, 0.24)).equals(new Point2D(0.2, 0.2)));
    StdOut.println(testPSET5.nearest(new Point2D(0.9, 0.9)));
    StdOut.printf("%-100s%-2s%n", "the nearest point to (0.9, 0.9) should be (0.5, 0.5)", testPSET5.nearest(new Point2D(0.9, 0.9)).equals(new Point2D(0.5, 0.5)));

  }

}