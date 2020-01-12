import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {

  private final int siteSize;
  private final int topRootId;
  private final int bottomRootId;
  private final WeightedQuickUnionUF unionFind;
  private boolean[] site;
  private int numberOfOpenSites = 0;
  private final WeightedQuickUnionUF unionFindForFull;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    siteSize = n;
    topRootId = 0;
    bottomRootId = n * n + 1;
    unionFind = new WeightedQuickUnionUF(n * n + 2);
    unionFindForFull = new WeightedQuickUnionUF(n * n + 2);
    site = new boolean[n * n + 2];
  }

  private void validateRowAndCol(int row, int col) {
    if (row < 1 || row > siteSize || col < 1 || col > siteSize) {
      throw new IllegalArgumentException();
    }
  }

  private int convert2DTo1DIndice(int row, int col) {
    return siteSize * (row - 1) + col;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {

    validateRowAndCol(row, col);

    if (isOpen(row, col)) {
      return;
    }

    int siteId = convert2DTo1DIndice(row, col);
    int aboveSiteId = siteId - siteSize;
    int belowSiteId = siteId + siteSize;
    int leftSiteId = siteId - 1;
    int rightSiteId = siteId + 1;

    // opens the site by siteId
    site[siteId] = true;
    numberOfOpenSites++;
    
    if (siteId <= siteSize) {
      unionFind.union(siteId, topRootId);
      unionFindForFull.union(siteId, topRootId);
    } else {
      if (site[aboveSiteId]) {
        unionFind.union(siteId, aboveSiteId);
        unionFindForFull.union(siteId, aboveSiteId);
      }
    }

    if (siteId > siteSize * (siteSize - 1)) {
      unionFind.union(siteId, bottomRootId);
    } else {
      if (site[belowSiteId]) {
        unionFind.union(siteId, belowSiteId);
        unionFindForFull.union(siteId, belowSiteId);
      }
    }

    if (siteId % siteSize != 1) {
      if (site[leftSiteId]) {
        unionFind.union(siteId, leftSiteId);
        unionFindForFull.union(siteId, leftSiteId);
      }
    }

    if (siteId % siteSize != 0) {
      if (site[rightSiteId]) {
        unionFind.union(siteId, rightSiteId);
        unionFindForFull.union(siteId, rightSiteId);
      }
    }

  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    validateRowAndCol(row, col);

    int siteId = convert2DTo1DIndice(row, col);

    return site[siteId];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    validateRowAndCol(row, col);

    int siteId = convert2DTo1DIndice(row, col);
    return unionFindForFull.find(siteId) == unionFindForFull.find(topRootId);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return numberOfOpenSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return unionFind.find(bottomRootId) == unionFind.find(topRootId);
  }

  // test client (optional)
  public static void main(String[] args) {
  //   Percolation percolation = new Percolation(20);
  //   percolation.open(6, 20);
  //   percolation.open(7, 16);
  //   for (int i = 1; i <= 20; i++) {
  //     for (int j = 1; j <= 20; j ++) {
  //       StdOut.println(percolation.isFull(i, j));
  //     }
  //   }
    Percolation percolation = new Percolation(3);
    percolation.open(1, 3);
    percolation.open(2, 3);
    percolation.open(3, 3);
    StdOut.println(percolation.isFull(3, 1));
    percolation.open(3, 1);
    percolation.open(3, 2);
    StdOut.println(percolation.isFull(3, 1));
    StdOut.println(percolation.percolates());
  }
}