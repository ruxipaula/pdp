package com.ppdlab2;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    LinkedList<Integer> list;
    int capacity;
    final Lock lock = new ReentrantLock();
    final Condition cond = lock.newCondition();

    public Buffer() {
        this.list = new LinkedList<>();
        this.capacity = 1;
    }

    public void add(int value) throws InterruptedException {
        lock.lock();
        while (list.size() == capacity) {
            cond.await();
        }
        list.add(value);
        System.out.println("Produced " + value);
        cond.signal();
        lock.unlock();
    }

    public int take() throws InterruptedException {
        lock.lock();
        while (list.size() == 0) {
            cond.await();
        }
        int value = list.removeFirst();
        System.out.println("Consumed " + value);
        cond.signal();
        lock.unlock();
        return value;
    }
}
