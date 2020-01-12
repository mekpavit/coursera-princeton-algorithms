import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;
  private final double[] percolationThresholds;
  private double cacheMean = -1.0;
  private double cacheStdDev = -1.0;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    percolationThresholds = new double[trials];
    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        int randomRow = StdRandom.uniform(1, n + 1);
        int randomCol = StdRandom.uniform(1, n + 1);
        percolation.open(randomRow, randomCol);
      }
      percolationThresholds[i] = percolation.numberOfOpenSites() / Double.valueOf(n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    if (cacheMean == -1) {
      cacheMean = StdStats.mean(percolationThresholds);
    }
    return cacheMean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    if (cacheStdDev == -1) {
      cacheStdDev = StdStats.stddev(percolationThresholds);
    }
    return cacheStdDev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(percolationThresholds.length));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(percolationThresholds.length));
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