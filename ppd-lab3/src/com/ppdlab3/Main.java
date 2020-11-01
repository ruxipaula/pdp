package com.ppdlab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> matrix1 = new ArrayList<>();
        List<List<Integer>> matrix2 = new ArrayList<>();
        int n = 3;
        int m = 3;

        createMatrices(matrix1, matrix2);

//        for (int i = 0; i < n; i++) {
//            matrix1.add(new ArrayList<>());
//            matrix2.add(new ArrayList<>());
//            for (int j = 0; j < m; j++) {
//                matrix1.get(i).add(j + 1);
//                matrix2.get(i).add(j - 1);
//            }
//        }

        MatrixUtilities mu = new MatrixUtilities(matrix1, matrix2, n, m);

        for (int i = 0; i < n; i++) {
            System.out.println(matrix1.get(i));
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println(matrix2.get(i));
        }
        System.out.println();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int pos = i;
//            Thread t = new Thread(() -> mu.computePartialResultLines(pos, 0, pos, 2));
            Thread t = new Thread(() -> mu.computePartialResultColumns(0, pos, 2, pos));
            threads.add(t);
        }

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

        for (int i = 0; i < n; i++) {
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < m; j++) {
                line.append(mu.getResult().get(i).get(j)).append(" ");
            }
            System.out.println(line);
        }
        System.out.println();
    }

    private static void createMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
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

}
