package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Percolation {

    private final int gridSize;
    private final boolean[] openSites;
    private final WeightedQuickUnionUF sitesUnion;
    private int openSitesCount;
    private final boolean[] connect2Top;
    private final boolean[] connect2Bottom;
    private boolean isPercolate;

    public Percolation(int N) {
        // create N-by-N grid, with all sites initially blocked
        sitesUnion = new WeightedQuickUnionUF(N * N + 2);
        openSites = new boolean[N * N];
        connect2Top = new boolean[N * N];
        connect2Bottom = new boolean[N * N];
        gridSize = N;
        openSitesCount = 0;
        isPercolate = false;
    }

    public void open(int row, int col) {
        // open the site (row, col) if it is not open already
        checkInput2DIndex(row, col);
        if (isOpen(row, col)) {
            return;
        }
        int centerIndex = row * gridSize + col;
        boolean connectedUnion2Top = false;
        boolean connectedUnion2Bottom = false;
        openSites[centerIndex] = true;
        openSitesCount += 1;

        int leftIndex = col == 0 ? -1 : row * gridSize + col - 1;
        int rightIndex = col == gridSize - 1 ? -1 : row * gridSize + col + 1;
        int topIndex = row == 0 ? -1 : (row - 1) * gridSize + col;
        int bottomIndex = row == gridSize - 1 ? -1 : (row + 1) * gridSize + col;
        if (leftIndex >= 0 && isOpen(row, col - 1)) {
            if (connect2Top[sitesUnion.find(leftIndex)]) {
                connectedUnion2Top = true;
            }
            if (connect2Bottom[sitesUnion.find(leftIndex)]) {
                connectedUnion2Bottom = true;
            }
            sitesUnion.union(leftIndex, centerIndex);
        }
        if (rightIndex >= 0 && isOpen(row, col + 1)) {
            if (connect2Top[sitesUnion.find(rightIndex)]) {
                connectedUnion2Top = true;
            }
            if (connect2Bottom[sitesUnion.find(rightIndex)]) {
                connectedUnion2Bottom = true;
            }
            sitesUnion.union(rightIndex, centerIndex);
        }
        if (topIndex >= 0 && isOpen(row - 1, col)) {
            if (connect2Top[sitesUnion.find(topIndex)]) {
                connectedUnion2Top = true;
            }
            if (connect2Bottom[sitesUnion.find(topIndex)]) {
                connectedUnion2Bottom = true;
            }
            sitesUnion.union(topIndex, centerIndex);
        }
        if (bottomIndex >= 0 && isOpen(row + 1, col)) {
            if (connect2Top[sitesUnion.find(bottomIndex)]) {
                connectedUnion2Top = true;
            }
            if (connect2Bottom[sitesUnion.find(bottomIndex)]) {
                connectedUnion2Bottom = true;
            }
            sitesUnion.union(bottomIndex, centerIndex);
        }
        if (row == 0) {
            connectedUnion2Top = true;
        }
        if (row == gridSize - 1) {
            connectedUnion2Bottom = true;
        }
        connect2Top[sitesUnion.find(centerIndex)] = connectedUnion2Top;
        connect2Bottom[sitesUnion.find(centerIndex)] = connectedUnion2Bottom;

        if (connect2Top[sitesUnion.find(centerIndex)] && connect2Bottom[sitesUnion.find(centerIndex)]) {
            isPercolate = true;
        }
    }


    public boolean isOpen(int row, int col) {
        // is the site (row, col) open?
        checkInput2DIndex(row, col);
        return openSites[row * gridSize + col];
    }

    public boolean isFull(int row, int col) {
        // is the site (row, col) full?
        checkInput2DIndex(row, col);
        return connect2Top[sitesUnion.find(row * gridSize + col)];
    }

    public int numberOfOpenSites() {
        return openSitesCount;
    }           // number of open sites

    public boolean percolates() {
        return isPercolate;
    }              // does the system percolate?

    private void checkOutOfBoundary(int i) {
        if (i >= gridSize || i < 0) {
            throw new IndexOutOfBoundsException(String.format("index must within [0, %d]", gridSize));
        }
    }

    private void checkInput2DIndex(int row, int col) {
        checkOutOfBoundary(row);
        checkOutOfBoundary(col);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(0, 0);
        p.open(0, 2);
        p.open(1, 2);
        System.out.println(p.percolates());
        p.open(2, 2);
        System.out.println(p.percolates());
        p.open(0, 2);
        System.out.println(p.isFull(0, 2));

    }// use for unit testing (not required, but keep this here for the autograder)
}