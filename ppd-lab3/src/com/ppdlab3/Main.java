package com.ppdlab3;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> matrix1 = new ArrayList<>();
        List<List<Integer>> matrix2 = new ArrayList<>();
        int n = 9;
        int m = 9;

        createMatrices(matrix1, matrix2, n, m);
        MatrixUtilities mu = new MatrixUtilities(matrix1, matrix2, n, m);

        List<Thread> threads = new ArrayList<>();
        List<List<Point>> results = new ArrayList<>();
        int nrThreads = 6;
        for (int i = 0; i < nrThreads; i++) {
            results.add(new ArrayList<>());
        }

//        createWorkload1(results, n, m, nrThreads);
//        createWorkload2(results, n, m, nrThreads);
        createWorkload3(results, n, m, nrThreads);

        for (int i = 0; i < nrThreads; i++) {
            int pos = i;
            Thread t = new Thread(() -> mu.computePartialResult(results.get(pos)));
            threads.add(t);
        }

//        runWithThreads(threads, n, m);
        runWithThreadPool(threads, n, m);
        printMatrices(mu, results);
    }

    public static void createWorkload1(List<List<Point>> results, int n, int m, int nrThreads) {
        int elemsForEachThread = m * n / nrThreads;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                int index = row * m + col;
                int thread = index / elemsForEachThread;
                if (thread == results.size()) {
                    results.get(nrThreads - 1).add(new Point(row, col, -1));
                } else {
                    results.get(thread).add(new Point(row, col, -1));
                }
            }
        }
    }

    public static void createWorkload2(List<List<Point>> results, int n, int m, int nrThreads) {
        int elemsForEachThread = m * n / nrThreads;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                int index = col * n + row;
                int thread = index / elemsForEachThread;
                if (thread == results.size()) {
                    results.get(nrThreads - 1).add(new Point(row, col, -1));
                } else {
                    results.get(thread).add(new Point(row, col, -1));
                }
            }
        }
    }

    public static void createWorkload3(List<List<Point>> results, int n, int m, int nrThreads) {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                int index = row * m + col;
                int thread = index % nrThreads;
                results.get(thread).add(new Point(row, col, -1));
            }
        }
    }

    public static void runWithThreads(List<Thread> threads, int n, int m) {
        long start = System.nanoTime();
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) + ", " + threads.size() + " threads");
    }

    public static void runWithThreadPool(List<Thread> threads, int n, int m) {
        int nrThreads = 4;
        ExecutorService service = Executors.newFixedThreadPool(nrThreads);

        long start = System.nanoTime();
        for (Thread t : threads) {
            service.execute(t);
        }

        service.shutdown();
        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) + ", " + threads.size() + " tasks, " + nrThreads + " threads - THREAD POOL");
    }

    public static void printMatrices(MatrixUtilities mu, List<List<Point>> results) {
        for (int i = 0; i < mu.getN(); i++) {
            System.out.println(mu.getMatrix1().get(i));
        }
        System.out.println();

        for (int i = 0; i < mu.getN(); i++) {
            System.out.println(mu.getMatrix2().get(i));
        }
        System.out.println();

        List<Point> matrix = new ArrayList<>();
        for (List<Point> result : results) {
            matrix.addAll(result);
        }

        Comparator<Point> compareByRow = Comparator.comparing(Point::getX);
        Comparator<Point> compareByColumn = Comparator.comparing(Point::getY);
        Comparator<Point> compareByPosition = compareByRow.thenComparing(compareByColumn);

        List<Point> finalMatrix = matrix.stream().sorted(compareByPosition).collect(Collectors.toList());
        Iterator<Point> iterator = finalMatrix.iterator();

        for (int i = 0; i < mu.getN(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < mu.getM(); j++) {
                line.append(iterator.next().getValue()).append(" ");
            }
            System.out.println(line);
        }
        System.out.println();
    }

    private static void createFixedMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        List<List<Integer>> dummy1 = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(1, 1, 2), Arrays.asList(4, 5, 2));
        List<List<Integer>> dummy2 = Arrays.asList(Arrays.asList(1, 3, 2), Arrays.asList(4, 1, 4), Arrays.asList(4, 2, 1));

        for (int i = 0; i < 3; i++) {
            matrix1.add(new ArrayList<>());
            matrix2.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                matrix1.get(i).add(dummy1.get(i).get(j));
                matrix2.get(i).add(dummy2.get(i).get(j));
            }
        }
    }

    private static void createMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2, int n, int m) {
        int min = 10;
        int max = 90;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            matrix1.add(new ArrayList<>());
            matrix2.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                matrix1.get(i).add(rand.nextInt((max - min) + 1) + min);
                matrix2.get(i).add(rand.nextInt((max - min) + 1) + min);
            }
        }
    }

}
