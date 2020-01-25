import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {

  private int numberOfSegments = 0;
  private LineSegment[] segments;

  public BruteCollinearPoints(Point[] points) {
    if (points == null) { throw new IllegalArgumentException(); }
    validatePoints(points);

    Arrays.sort(points);

    LineSegment[] lineSegments = new LineSegment[points.length * points.length];
    for (int i = 0; i < points.length - 3; i++) {
      for (int j = i + 1; j < points.length - 2; j++) {
        for (int k = j + 1; k < points.length - 1; k++) {
          for (int l = k + 1; l < points.length; l++) {
            double slopeOfP1P2 = points[i].slopeTo(points[j]);
            double slopeOfP1P3 = points[i].slopeTo(points[k]);
            double slopeOfP1P4 = points[i].slopeTo(points[l]);
            if (slopeOfP1P2 == slopeOfP1P3 && slopeOfP1P2 == slopeOfP1P4) {
              lineSegments[numberOfSegments] = new LineSegment(points[i], points[l]);
              numberOfSegments++;
            }
          }
        }
      }
    }

    segments = new LineSegment[numberOfSegments];
    for (int i = 0; i < numberOfSegments; i++) {
      segments[i] = lineSegments[i];
    }

  }

  private void validatePoints(Point[] points) {
    Point[] copyOfPoints = new Point[points.length];

    for (int i = 0; i < points.length; i++) {
      if (points[i] == null) { throw new IllegalArgumentException(); }
      copyOfPoints[i] = points[i];
    }

    Arrays.sort(copyOfPoints);
    for (int i = 1; i < copyOfPoints.length; i++) {
      if (copyOfPoints[i] == copyOfPoints[i - 1]) {
        throw new IllegalArgumentException();
      }
    }

  }

  public int numberOfSegments() {
    return numberOfSegments;
  }

  public LineSegment[] segments() {
    LineSegment[] copyOfSegments = new LineSegment[numberOfSegments];
    for (int i = 0; i < numberOfSegments; i++) {
      copyOfSegments[i] = segments[i];
    }
    return copyOfSegments;
  }

  public static void main(String[] args) {
    // // numberOfSegments
    // System.out.println("numberOfSegments");
    // Point[] points = {new Point(1, 2), new Point(1, 1), new Point(2, 2), new Point(2, 4), new Point(3, 3), new Point(3, 6), new Point(4, 4), new Point(4, 8)};
    // BruteCollinearPoints bcp = new BruteCollinearPoints(points);
    // System.out.printf("%-100s%-1s%n", "numberOfSegments should return 2", bcp.numberOfSegments() == 2);

    // System.out.println(bcp.segments()[0].toString());
    // System.out.println(bcp.segments()[1].toString());
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();

  }

}