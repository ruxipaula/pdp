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

    public void computePartialResult(List<Point> result) {
        for(Point p: result) {
            int elem = constructOneElement(p.getX(), p.getY());
            p.setValue(elem);
        }
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
