package com.ppdlab2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        List<Integer> v1 = new ArrayList<>();
        List<Integer> v2 = new ArrayList<>();

        v1.add(1);
        v1.add(2);
        v1.add(3);
        v1.add(3);

        v2.add(9);
        v2.add(2);
        v2.add(1);
        v2.add(2);

        Producer p = new Producer(buffer, v1, v2);
        Consumer c = new Consumer(buffer, v1.size());

        p.start();
        c.start();

        try {
            p.join();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(v1);
        System.out.println(v2);
        System.out.println("Scalar product => " + c.getSum());
    }
}
