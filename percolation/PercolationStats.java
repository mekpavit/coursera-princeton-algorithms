import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;
  private int sampleSize;
  private double cacheMean;
  private double cacheStdDev;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    sampleSize = trials;
    double[] percolationThresholds = new double[trials];
    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        int randomRow = StdRandom.uniform(1, n + 1);
        int randomCol = StdRandom.uniform(1, n + 1);
        percolation.open(randomRow, randomCol);
      }
      percolationThresholds[i] = percolation.numberOfOpenSites() / Double.valueOf(n * n);
    }
    cacheMean = StdStats.mean(percolationThresholds);
    cacheStdDev = StdStats.stddev(percolationThresholds);
  }

  // sample mean of percolation threshold
  public double mean() {
    return cacheMean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return cacheStdDev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return cacheMean - (CONFIDENCE_95 * cacheStdDev / Math.sqrt(sampleSize));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return cacheMean + (CONFIDENCE_95 * cacheStdDev / Math.sqrt(sampleSize));
  }

 // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats sim = new PercolationStats(n, trials);
    String format = "%-30s%-2s%f%n";
    StdOut.printf(format, "mean", "=", sim.mean());
    StdOut.printf(format, "stddev", "=", sim.stddev());
    StdOut.printf("%-30s%-2s%s%f%s%f%s%n", "95% confidence interval", "=", "[", sim.confidenceLo(), ", ", sim.confidenceHi(), "]");
  }

}