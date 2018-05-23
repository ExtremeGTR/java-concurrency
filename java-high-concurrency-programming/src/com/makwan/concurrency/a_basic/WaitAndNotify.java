package com.makwan.concurrency.a_basic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 线程间的协作通信最重要的两个方法: wait和notify
 *   - 这两个方法都不属于Thread类, 而是属于Object类,
 *     这表示所有Object或其子类的对象都可以调用它们
 *
 *   -
 *
 * 2018/5/23 16:37
 *
 * @author plaYwiThsouL
 */
public class WaitAndNotify {
    public static void main(String[] args) {
        // 这个
        Thread threadA = new Thread(() -> {
            synchronized (OBJECT) {
                printInfo("start!");
                System.out.println(FORMAT.format(new Date()) + ": thread-A start!");
                try {
                    printInfo("wait for OBJECT!!!");
                    OBJECT.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printInfo("end!");
            }
        }, "thread-A");
        Thread threadB = new Thread(() -> {
            synchronized (OBJECT) {
                printInfo("start! notify one thread!!!");
                OBJECT.notify();
                printInfo("end!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread-B");

        threadA.start();
        threadB.start();
    }

    private static final Object OBJECT = new Object();

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void printInfo(String action) {
        System.out.println(FORMAT.format(new Date()) + ": "
                + Thread.currentThread().getName() + " " + action);
    }
}