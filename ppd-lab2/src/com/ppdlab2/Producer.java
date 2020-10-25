package com.ppdlab2;

import java.util.List;

public class Producer extends Thread {
    private Buffer buffer;
    private List<Integer> v1;
    private List<Integer> v2;

    public Producer(Buffer buffer, List<Integer> v1, List<Integer> v2) {
        super("Producer");
        this.buffer = buffer;
        this.v1 = v1;
        this.v2 = v2;
    }

    public void run() {
        for (int i = 0; i < v1.size(); i++) {
            try {
                int product = v1.get(i) * v2.get(i);
                buffer.add(product);
//                System.out.println(getName() + " produced " + product);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
