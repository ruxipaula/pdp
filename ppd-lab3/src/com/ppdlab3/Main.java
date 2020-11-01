package com.ppdlab3;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> matrix1 = new ArrayList<>();
        List<List<Integer>> matrix2 = new ArrayList<>();
        int n = 5;
        int m = 5;

        createMatrices(matrix1, matrix2, n, m);
        MatrixUtilities mu = new MatrixUtilities(matrix1, matrix2, n, m);

        List<Thread> threads = new ArrayList<>();
        List<List<Point>> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int pos = i;
            results.add(new ArrayList<>());
//            Thread t = new Thread(() -> mu.computePartialResultLines(pos, 0, pos, n-1, results.get(pos)));
            Thread t = new Thread(() -> mu.computePartialResultColumns(0, pos, m-1, pos, results.get(pos)));
//            Thread t = new Thread(() -> mu.computePartialResultKElement(nrThreads, pos, results.get(pos)));
            threads.add(t);
        }

        runWithThreads(threads, n, m);
//        runWithThreadPool(threads, n, m);
        printMatrices(mu, results);
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
        System.out.println("Results: " + (end - start) + ", " + threads.size() + " threads");
    }

    public static void runWithThreadPool(List<Thread> threads, int n, int m) {
        ExecutorService service = Executors.newFixedThreadPool(threads.size());
        for(Thread t : threads) {
            service.execute(t);
        }

        service.shutdown();
    }

    public static void printMatrices(MatrixUtilities mu, List<List<Point>> results) {
        for (int i = 0; i < mu.getN() ; i++) {
            System.out.println(mu.getMatrix1().get(i));
        }
        System.out.println();

        for (int i = 0; i < mu.getN() ; i++) {
            System.out.println(mu.getMatrix2().get(i));
        }
        System.out.println();

        List<Point> matrix = new ArrayList<>();
        for (List<Point> result: results) {
            matrix.addAll(result);
        }

        Comparator<Point> compareByRow = Comparator.comparing( Point::getX );
        Comparator<Point> compareByColumn = Comparator.comparing( Point::getY );
        Comparator<Point> compareByPosition = compareByRow.thenComparing(compareByColumn);

        List<Point> finalMatrix = matrix.stream().sorted(compareByPosition).collect(Collectors.toList());
        Iterator<Point> iterator = finalMatrix.iterator();

        for (int i = 0; i < mu.getN(); i++) {
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < mu.getM(); j++) {
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
