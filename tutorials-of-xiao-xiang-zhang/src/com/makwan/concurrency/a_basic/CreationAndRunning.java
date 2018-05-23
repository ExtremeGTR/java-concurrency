package com.makwan.concurrency.a_basic;

/**
 * 线程的创建和启动
 *   - 有两种方式给线程指定待执行的任务
 *
 * 2018/5/7 22:28
 *
 * @author plaYwiThsouL
 */
public class CreationAndRunning {
    public static void main(String[] args) {
        // 通过重写Thread.run指定线程待执行的任务
        Thread threadA = new Thread("threadA") {
            @Override
            public void run() {
                int count = 20;
                while (count-- > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(getId() + " : " + getName());
                }
            }
        };
        threadA.start();

        // 通过传递实现Runnable.run的实例指定线程待执行的任务
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 20;
                while (count-- > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId()
                            + " : " + Thread.currentThread().getName());
                }
            }
        }, "threadB");
        threadB.start();

        // 同时使用两种方式给同一个线程指定待执行任务, 最终会执行哪一个?
        // 理解关键点: 1-继承重写 2-Thread.run源码
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 20;
                while (count-- > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Runnable.run -> " + Thread.currentThread().getName());
                }
            }
        }, "threadC") {
            @Override
            public void run() {
                int count = 20;
                while (count-- > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread.run -> " + getName());
                }
            }
        };
        threadC.start();
    }
}