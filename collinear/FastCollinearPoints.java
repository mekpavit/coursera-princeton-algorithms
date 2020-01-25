import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {

  private int numberOfSegments = 0;
  private LineSegment[] segments;

  public FastCollinearPoints(Point[] points) {

    if (points == null) { throw new IllegalArgumentException(); }

    validatePoints(points);

    double[] colinearSlope = new double[points.length * points.length];
    LineSegment[] lineSegments = new LineSegment[points.length * points.length];
    for (int i = 0; i < points.length; i++) {
      Arrays.sort(points);
      Point p = points[i];
      Arrays.sort(points, p.slopeOrder());

      double currentSlope = p.slopeTo(points[1]);
      int numberOfPointsWithCurrentSlope = 1;
      for (int j = 2; j < points.length; j++) {
        if (currentSlope != p.slopeTo(points[j]) || j == points.length - 1) {
          int lastIndexOfCurrentSlope = j - 1;
          if (currentSlope == p.slopeTo(points[j]) && j == points.length - 1) {
            lastIndexOfCurrentSlope = j;
          }

          if (numberOfPointsWithCurrentSlope >= 3) {
            boolean isDuplicated = false;
            for (int k = 0; k < numberOfSegments; k++) {
              if (currentSlope == colinearSlope[k]) {
                isDuplicated = true;
              }
            }
            if (!isDuplicated) {
              colinearSlope[numberOfSegments] = currentSlope;
              lineSegments[numberOfSegments] = new LineSegment(p, points[lastIndexOfCurrentSlope]);
              numberOfSegments++;
            }
          }
          currentSlope = p.slopeTo(points[j]);
          numberOfPointsWithCurrentSlope = 1;
        } else {
          numberOfPointsWithCurrentSlope++;
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
    // Point[] points = {new Point(1, 2), new Point(1, 1), new Point(2, 2), new Point(2, 4), new Point(3, 3), new Point(3, 6), new Point(4, 4), new Point(4, 8), new Point(5, 5), new Point(10, 20), new Point(100, 100)};
    // FastCollinearPoints bcp = new FastCollinearPoints(points);
    // System.out.printf("%-100s%-1s%n", "numberOfSegments should return 2", bcp.numberOfSegments() == 2);
    // System.out.println(bcp.segments()[0]);
    // System.out.println(bcp.segments()[1]);

    // // numberOfSegments
    // System.out.println("numberOfSegments");
    // Point[] points2 = {new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5), new Point(6, 6), new Point(7, 7), new Point(8, 8), new Point(9, 9), new Point(10, 10), new Point(11, 11)};
    // FastCollinearPoints bcp2 = new FastCollinearPoints(points2);
    // System.out.printf("%-100s%-1s%n", "numberOfSegments should return 2", bcp2.numberOfSegments() == 1);
    // System.out.println(bcp2.segments()[0]);
    // System.out.println(bcp2.segments()[1]);

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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();

  }

}