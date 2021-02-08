package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] percolationThreshold;
    private int times;

    public PercolationStats(int N, int T, PercolationFactory pf)
    // perform T independent experiments on an N-by-N grid
    {
        Percolation percolation = pf.make(N);
        percolationThreshold = new double[T];
        times = T;
        for (int t = 0; t < T; t++) {
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            percolationThreshold[t] = (double) percolation.numberOfOpenSites() / (N * N);
        }
    }

    public double mean()
    // sample mean of percolation threshold
    {
        return StdStats.mean(percolationThreshold);
    }

    public double stddev()
    // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(percolationThreshold);
    }

    public double confidenceLow()
    // low endpoint of 95% confidence interval
    {
        return mean() - 1.96 * stddev() / Math.sqrt(times);
    }

    public double confidenceHigh()
    // high endpoint of 95% confidence interval
    {
        return mean() + 1.96 * stddev() / Math.sqrt(times);
    }
}

