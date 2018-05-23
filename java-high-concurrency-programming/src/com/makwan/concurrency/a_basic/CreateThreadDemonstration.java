package com.makwan.concurrency.a_basic;

import org.junit.Test;

/**
 * 2018/5/13 14:33
 *
 * @author plaYwiThsouL
 */
public class CreateThreadDemonstration {
    // 最简单的创建线程
    @Test
    public void createThreadA() {
        // 创建新线程
        Thread thread = new Thread();
        // 让新线程运行起来: 实际上这是让Java虚拟机运行新线程并调用该线程里的run方法
        thread.start();
    }

    @Test
    public void createThreadProblem() {
        Thread thread = new Thread();
        /* 通过新建的线程对象直接调用run方法
         * 这相当于在当前线程串行执行run方法, 而不是通过新建线程执行它
         */
        thread.run();
    }

    // 通过重写Thread.run指定线程待执行的任务
    // 但Java只能是单继承, 所以说继承本身就是一种宝贵的资源,
    // 所以一般不使用这种方法给线程指定待执行的任务
    @Test
    public void createThreadB() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("this is a new thread ");
            }
        };
        thread.start();
    }

    // 通过使用Runnable来指定线程待执行的任务更加合理
    // 创建线程并指定其所要执行的任务最常用的就是这个方式
    @Test
    public void createThreadC() {
        // 通过创建Runnable的具体类实现
        class Task implements Runnable {
            @Override
            public void run() {
                System.out.println("concrete class");
            }
        }
        Thread threadA = new Thread(new Task());
        threadA.start();

        // 通过创建Runnable的匿名类实现
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("anonymous class");
            }
        });
        threadB.start();
    }
}