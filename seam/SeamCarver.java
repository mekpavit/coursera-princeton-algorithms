import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

  private Picture picture;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null) {throw new IllegalArgumentException();}

    this.picture = new Picture(picture);
  }

  // current picture
  public Picture picture() {
    return this.picture;
  }

  // width of current picture
  public int width() {
    return this.picture.width();
  }

  // height of current picture
  public int height() {
    return this.picture.height();
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    this.validateX(x);
    this.validateY(y);

    if (x == 0 || y == 0 || x == this.width() - 1 || y == this.height() - 1) {
      return 1000.0;
    }

    int leftRGB = this.picture.getRGB(x - 1, y);
    int leftR = (leftRGB >> 16) & 0xFF;
    int leftG = (leftRGB >> 8) & 0xFF;
    int leftB = (leftRGB >> 0) & 0xFF;
    int rightRGB = this.picture.getRGB(x + 1, y);
    int rightR = (rightRGB >> 16) & 0xFF;
    int rightG = (rightRGB >> 8) & 0xFF;
    int rightB = (rightRGB >> 0) & 0xFF;
    double deltaX = Math.pow(leftR - rightR, 2) + Math.pow(leftG - rightG, 2) + Math.pow(leftB - rightB, 2);

    int topRGB = this.picture.getRGB(x, y + 1);
    int topR = (topRGB >> 16) & 0xFF;
    int topG = (topRGB >> 8) & 0xFF;
    int topB = (topRGB >> 0) & 0xFF;
    int bottomRGB = this.picture.getRGB(x, y - 1);
    int bottomR = (bottomRGB >> 16) & 0xFF;
    int bottomG = (bottomRGB >> 8) & 0xFF;
    int bottomB = (bottomRGB >> 0) & 0xFF;
    double deltaY = Math.pow(topR - bottomR, 2) + Math.pow(topG - bottomG, 2) + Math.pow(topB - bottomB, 2);

    return Math.sqrt(deltaX + deltaY);
  }

  private void validateX(int x) {
    if (x < 0 || x >= this.width()) { throw new IllegalArgumentException(); }
  }

  private void validateY(int y) {
    if (y < 0 || y >= this.height()) { throw new IllegalArgumentException(); }
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    this.transpose();
    int[] horizontalSeam = this.findVerticalSeam();
    this.transpose();
    return horizontalSeam;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    int width = this.width();
    int height = this.height();
    
    int[][] edgeTo = new int[width][height];
    double[][] distTo = new double[width][height];
    double[][] energy = new double[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (y == 0) {
          distTo[x][y] = 1000;
          edgeTo[x][y] = -1;
        } else {
          distTo[x][y] = Double.MAX_VALUE;
          energy[x][y] = -1;
        }
      }
    }
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.relaxAllEdgesFrom(x, y, edgeTo, distTo, energy);
      }
    }
    double shortestDistToOnLastRow = Double.MAX_VALUE;
    int shortestXOnLastRow = -1;
    for (int x = 0; x < width; x++) {
      if (shortestDistToOnLastRow > distTo[x][height - 1]) {
        shortestDistToOnLastRow = distTo[x][height - 1];
        shortestXOnLastRow = x;
      }
    }
    int[] verticalSeam = new int[height];
    int y = height;
    for (int x = shortestXOnLastRow; x != -1; x = edgeTo[x][y]) {
      y--;
      verticalSeam[y] = x;
    }
    return verticalSeam;
  }

  private void relaxAllEdgesFrom(int x, int y, int[][] edgeTo, double[][] distTo, double[][] energy) {
    int width = this.width();
    int height = this.height();

    for (int relC = -1; relC <= 1; relC++) {
      if (x + relC < 0 || y + 1 < 0 || x + relC > width - 1 || y + 1 > height - 1) {
        continue;
      }
      if (energy[x + relC][y + 1] == -1.0) {
        energy[x + relC][y + 1] = this.energy(x + relC, y + 1);
      }
      
      if (distTo[x + relC][y + 1] > distTo[x][y] + energy[x + relC][y + 1]) {
        distTo[x + relC][y + 1] = distTo[x][y] + energy[x + relC][y + 1];
        edgeTo[x + relC][y + 1] = x;
      }
    }
  }

  private void transpose() {
    Picture newPicture = new Picture(this.height(), this.width());
    for (int r = 0; r < this.height(); r++) {
      for (int c = 0; c < this.width(); c++) {
        newPicture.setRGB(r, c, this.picture.getRGB(c, r));
      }
    }
    this.picture = newPicture;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    this.transpose();
    this.removeVerticalSeam(seam);
    this.transpose();
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (seam == null) { throw new IllegalArgumentException(); }
    if (seam.length != this.height()) { throw new IllegalArgumentException(); }
    if (this.width() <= 1) { throw new IllegalArgumentException(); }

    int width = this.width();
    int height = this.height();
    Picture newPicture = new Picture(width - 1, height);
    for (int r = 0; r < height; r++) {
      int newPictureColIdx = 0;
      for (int c = 0; c < width; c++) {
        if (c != seam[r]) {
          newPicture.setRGB(newPictureColIdx, r, this.picture.getRGB(c, r));
          newPictureColIdx++;
        }
      }
    }

    this.picture = newPicture;
  }

  //  unit testing (optional)
  public static void main(String[] args) {

  }

}