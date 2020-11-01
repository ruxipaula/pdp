package com.ppdlab3;

import java.util.List;

public class MatrixUtilities {
    private List<List<Integer>> matrix1;
    private List<List<Integer>> matrix2;
    private int n;
    private int m;

    public MatrixUtilities(List<List<Integer>> matrix1, List<List<Integer>> matrix2, int n, int m) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.n = n;
        this.m = m;
    }

    public int constructOneElement(int row, int column) {
        int result = 0;
        for (int i = 0; i < m; i++) {
            result += matrix1.get(row).get(i) * matrix2.get(i).get(column);
        }
        return result;
    }

    public void computePartialResultLines(int startX, int startY, int endX, int endY, List<Point> result) {
        for (int i = startX; i <= endX; i++) {
            int firstColumn;
            if (i == startX) {
                firstColumn = startY;
            } else {
                firstColumn = 0;
            }
            int lastColumn;
            if (i == endX) {
                lastColumn = endY;
            } else {
                lastColumn = m - 1;
            }

            for (int j = firstColumn; j <= lastColumn; j++) {
                int element = constructOneElement(i, j);
                result.add(new Point(i, j, element));
            }
        }
    }

    public void computePartialResultColumns(int startX, int startY, int endX, int endY, List<Point> result) {
        for (int j = startY; j <= endY; j++) {
            int firstRow;
            if (j == startY) {
                firstRow = startX;
            } else {
                firstRow = 0;
            }
            int lastRow;
            if (j == endY) {
                lastRow = endX;
            } else {
                lastRow = n - 1;
            }

            for (int i = firstRow; i <= lastRow; i++) {
                int element = constructOneElement(i, j);
                result.add(new Point(i, j, element));
            }
        }
    }

    public void computePartialResultKElement(int k, int threadNr, List<Point> result) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(index(i, j) % k == threadNr) {
                    int element = constructOneElement(i, j);
                    result.add(new Point(i, j, element));
                }
            }
        }
    }

    private int index(int row, int col) {
        return row * m + col;
    }

    public List<List<Integer>> getMatrix1() {
        return matrix1;
    }

    public List<List<Integer>> getMatrix2() {
        return matrix2;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
}
