package com.makwan.concurrency.a_basic;

/**
 * 线程的互斥和同步通信
 *   - 需求: 首先在1个子线程内循环执行某任务3次, 然后在主线程内循环执行某任务5次,
 *           并且, 不能让子线程和主线程并发地执行各自的任务, 它们之间必须交替地执行各自的任务,
 *           即子线程执行时, 主线程就不能执行, 反之亦然, 以上这个过程循环20次.
 *
 *   - 对比命令式和面向对象两种实现风格,
 *     可以立刻发现, 面向对象风格的代码更易于理解和维护以及重用
 *
 * 2018/5/18 10:13
 *
 * @author plaYwiThsouL
 */
public class ThreadCommunication {
    public static void main(String[] args) {
        implementWithImperativeStyle();
    }

    // 命令式风格的实现
    private static void implementWithImperativeStyle() {
        Class<ThreadCommunication> c = ThreadCommunication.class;
        new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                synchronized (c) {
                    while (!subExecute) {
                        try {
                            c.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int j = 1; j <= 3; j++) {
                        System.out.println("sub thread execute " + j + " time(s) of loop-" + i);
                    }
                    subExecute = false;
                    c.notify();
                }
            }
        }).start();

        for (int i = 1; i <= 20; i++) {
            synchronized (c) {
                while (subExecute) {
                    try {
                        c.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 5; j++) {
                    System.out.println("main thread execute " + j + " time(s) of loop-" + i);
                }
                subExecute = true;
                c.notify();
            }
        }
    }

    // 面向对象风格的实现
    private static void implementWithObjectOrientedStyle() {
        Business business = new Business();
        new Thread(() -> {
            for (int i = 1; i < 20; i++) {
                business.executeSubTask(i);
            }
        }).start();
        for (int i = 1; i < 20; i++) {
            business.executeMainTask(i);
        }
    }

    private static boolean subExecute = true;

    // 将多线程的互斥和通信逻辑都封装进一个类里
    private static class Business {
        private boolean shouldSubExecute = true;

        synchronized void executeSubTask(int i) {
            while (!shouldSubExecute) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 1; j <= 3; j++) {
                System.out.println("execute sub task " + j + " time(s) of loop-" + i);
            }
            shouldSubExecute = false;
            this.notify();
        }

        synchronized void executeMainTask(int i) {
            while (shouldSubExecute) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 1; j < 5; j++) {
                System.out.println("execute main task " + j + " time(s) of loop-" + i);
            }
            shouldSubExecute = true;
            this.notify();
        }

    }
}