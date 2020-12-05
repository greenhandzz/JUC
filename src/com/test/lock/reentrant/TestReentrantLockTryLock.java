package com.test.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLockTryLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " lock");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                // 注意tryLock()方法对于公平锁/非公平锁是一样的，
                // 即使当前是公平锁，且等待队列里有其他线程在等待，只要锁没有被hold，
                // 那么调用tryLock的线程会先于等待队列中的线程获取锁定
                // 但tryLock(long timeout, TimeUnit unit)方法对于公平锁/非公平锁是不一样的，
                // 如果是公平锁，则调用tryLock(long timeout, TimeUnit unit)的线程会进入等待尾端
                //
                /*If you want a timed {@code tryLock} that does permit barging  on
                 * a fair lock then combine the timed and un-timed forms together:
                 *
                 * <pre> {@code
                 * if (lock.tryLock() ||
                 *     lock.tryLock(timeout, unit)) {
                 *   ...
                 * }}</pre>
                 */
                lock.tryLock(2, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " lock");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }, "T2").start();
    }
}
