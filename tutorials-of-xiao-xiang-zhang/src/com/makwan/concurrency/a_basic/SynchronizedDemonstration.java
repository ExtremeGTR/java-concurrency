package com.makwan.concurrency.a_basic;

import org.junit.Test;

/**
 * 线程互斥
 *   - 互斥指的是多个线程对共享资源互斥地访问
 *
 *   - 使用synchronized实现互斥, 有两种实现手段
 *     1.将需要互斥访问的代码用synchronized块包裹起来
 *     2.用synchronized修饰方法
 *
 * 2018/5/9 15:45
 *
 * @author plaYwiThsouL
 */
public class SynchronizedDemonstration {
    // 共享资源
    private static int count = 0;

    // 直接用多个线程操作同一个共享资源
    // 那是铁定会出现同步错误
    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    count++;
                }
                System.out.println(Thread.currentThread().getName() + " : count = " + count);
            }).start();
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronized (this) {
                        count++;
                    }
                }
                System.out.println(Thread.currentThread().getName() + " : count = " + count);
            }).start();
        }
    }
}