package com.test.lock.reentrant;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLockFairOrNot {
    // ReentrantLock 默认是非公平锁（效率高），修改true/false，看效果
    ReentrantLock mLock = new ReentrantLock(false);
    private int mCount;

    public static void main(String[] args) {
        TestReentrantLockFairOrNot test = new TestReentrantLockFairOrNot();
//        new Thread(test::1, "T" + 1).start();
//        new Thread(test::m1, "T" + 2).start();

//        // 使用公平锁可以让多个线程依次打印 1-100之间的数
        new Thread(test::m2, "T" + 1).start();
        new Thread(test::m2, "T" + 2).start();
        new Thread(test::m2, "T" + 3).start();
        new Thread(test::m2, "T" + 4).start();
        new Thread(test::m2, "T" + 5).start();


        // 使用synchronized 也可以让多个线程依次打印 1-100之间的数，但顺序就控制不了，因为notify()方法是随机唤醒的
        // 而且因为最后使用了wait()方法，导致线程一直在运行状态，程序一直没有结束
//        new Thread(test::m3, "T" + 1).start();
//        new Thread(test::m3, "T" + 2).start();
//        new Thread(test::m3, "T" + 3).start();
//        new Thread(test::m3, "T" + 4).start();
//        new Thread(test::m3, "T" + 5).start();
    }

    private void m1() {
        for (int i = 0; i < 100; i++) {
            try {
                mLock.lock();
                System.out.println(Thread.currentThread().getName() + " lock " + i);
            } finally {
                if (mLock.isHeldByCurrentThread()) {
                    mLock.unlock();
                }
            }
        }
    }

    private void m2() {
        while (mCount < 100) {
            try {
                mLock.lock();
                System.out.println(Thread.currentThread().getName() + " " + ++mCount);
            } finally {
                if (mLock.isHeldByCurrentThread()) {
                    mLock.unlock();
                }
            }
        }
    }

    private void m3() {
        while (mCount < 100) {
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " " + ++mCount);
                try {
                    notify();
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
