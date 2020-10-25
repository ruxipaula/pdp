package com.ppdlab2;

public class Consumer extends Thread {
    private Buffer buffer;
    private int sum;
    private int toConsume;

    public Consumer(Buffer buffer, int number) {
        super("Consumer");
        this.buffer = buffer;
        this.sum = 0;
        this.toConsume = number;
    }

    public void run() {
        while (toConsume > 0) {
            try {
                int value = buffer.take();
//                System.out.println(getName() + " consumed " + value);
                sum += value;
                toConsume--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSum() {
        return sum;
    }
}
