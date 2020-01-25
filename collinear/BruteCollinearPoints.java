import java.util.Arrays;

public class BruteCollinearPoints {

  private int numberOfSegments = 0;
  private final LineSegment[] lineSegments;

  public BruteCollinearPoints(Point[] points) {
    if (points == null) { throw new IllegalArgumentException(); }

    Arrays.sort(points);

    for (int i = 0; i < points.length - 3; i++) {
      validatePoint(points[i]);
      for (int j = i + 1; j < points.length - 2; j++) {
        validatePoint(points[j]);
        for (int k = j + 1; k < points.length - 1; k++) {
          validatePoint(points[k]);
          for (int l = k + 1; l < points.length; l++) {
            validatePoint(points[l]);
            double slopeOfP1P2 = points[i].slopeTo(points[j]);
            double slopeOfP1P3 = points[i].slopeTo(points[k]);
            double slopeOfP1P4 = points[i].slopeTo(points[l]);
            if (slopeOfP1P2 == slopeOfP1P3 && slopeOfP1P2 == slopeOfP1P4) {
              numberOfSegments++;
            }
          }
        }
      }
    }

    lineSegments = new LineSegment[numberOfSegments];
    int lineSegmentsIndex = 0;
    for (int i = 0; i < points.length - 3; i++) {
      for (int j = i + 1; j < points.length - 2; j++) {
        for (int k = j + 1; k < points.length - 1; k++) {
          for (int l = k + 1; l < points.length; l++) {
            double slopeOfP1P2 = points[i].slopeTo(points[j]);
            double slopeOfP1P3 = points[i].slopeTo(points[k]);
            double slopeOfP1P4 = points[i].slopeTo(points[l]);
            if (slopeOfP1P2 == slopeOfP1P3 && slopeOfP1P2 == slopeOfP1P4) {
              lineSegments[lineSegmentsIndex] = new LineSegment(points[i], points[l]);
              lineSegmentsIndex++;
            }
          }
        }
      }
    }

  }

  private void validatePoint(Point p) {
    if (p == null) { throw new IllegalArgumentException(); }
  }

  public int numberOfSegments() {
    return numberOfSegments;
  }

  public LineSegment[] segments() {
    return lineSegments;
  }

  public static void main(String[] args) {
    // numberOfSegments
    System.out.println("numberOfSegments");
    Point[] points = {new Point(1, 2), new Point(1, 1), new Point(2, 2), new Point(2, 4), new Point(3, 3), new Point(3, 6), new Point(4, 4), new Point(4, 8)};
    BruteCollinearPoints bcp = new BruteCollinearPoints(points);
    System.out.printf("%-100s%-1s%n", "numberOfSegments should return 2", bcp.numberOfSegments() == 2);

    System.out.println(bcp.segments()[0].toString());
    System.out.println(bcp.segments()[1].toString());

    // // segments
    // System.out.println("segments");
    // System.out.printf("%-100s%-1s%n", "segments should return correct first segment", bcp.segments()[0].toString() == "(1, 1) -> (4, 4)");
    // System.out.printf("%-100s%-1s%n", "segments should return correct second segment", bcp.segments()[1].toString() == "(1, 2) -> (4, 8)");
  }

}