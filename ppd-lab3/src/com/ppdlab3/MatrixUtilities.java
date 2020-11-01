package com.ppdlab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class MatrixUtilities {
    private List<List<Integer>> matrix1;
    private List<List<Integer>> matrix2;
    private List<Map<Integer, Integer>> result;
    private int n;
    private int m;
    private ReentrantLock lock;

    public MatrixUtilities(List<List<Integer>> matrix1, List<List<Integer>> matrix2, int n, int m) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = new ArrayList<>();
        for (int i = 0; i < matrix1.size(); i++) {
            this.result.add(new HashMap<>());
        }
        this.n = n;
        this.m = m;
        this.lock = new ReentrantLock();
    }

    public List<Map<Integer, Integer>> getResult() {
        return result;
    }

    public int constructOneElement(int row, int column) {
        int result = 0;
        for (int i = 0; i < m; i++) {
            result += matrix1.get(row).get(i) * matrix2.get(i).get(column);
        }
        return result;
    }

    public void computePartialResultLines(int startX, int startY, int endX, int endY) {
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
                result.get(i).put(j, element);
            }
        }
    }

    public void computePartialResultColumns(int startX, int startY, int endX, int endY) {
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
                lock.lock();
                result.get(i).put(j, element);
                lock.unlock();
            }
        }
    }

    public void computePartialResultKElement(int startX, int startY, int k) {
        int x = startX;
        int y = startY;
        while (x <= n && y <= m) {
            int element = constructOneElement(x, y);
            result.get(x).put(y, element);
            if (y + k > m) {
                y = y + k - m;
            }
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                int element = constructOneElement(i, j);
                result.get(i).put(j, element);
            }
        }
    }
}
