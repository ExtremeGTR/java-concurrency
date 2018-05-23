package com.makwan.concurrency.a_basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 自己实现一个功能: 绑定于线程的数据
 *   - 在同一数据模型下, 每个线程都拥有互不相干的数据
 *
 * 2018/5/19 10:17
 *
 * @author plaYwiThsouL
 */
public class DataBinding {
    private static volatile Map<Thread, Object> threadData = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                int data = new Random().nextInt();
                threadData.put(Thread.currentThread(), data);
                System.out.println(Thread.currentThread().getName() + " put data: " + data);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                printData();
            }).start();
        }
    }

    private static void printData() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "'s data: " + threadData.get(thread));
    }
}