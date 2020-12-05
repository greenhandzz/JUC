package com.test.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLockInterruptibly {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " lock");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("T1 InterruptedException" + e);
            } finally {
                lock.unlock();
            }
        }, "T1");
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " lock");
            } catch (InterruptedException e) {
                System.out.println("T2 InterruptedException");
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }, "T2");
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.interrupt();
    }
}
