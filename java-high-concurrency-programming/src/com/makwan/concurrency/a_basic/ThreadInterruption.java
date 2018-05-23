package com.makwan.concurrency.a_basic;

/**
 * 线程中断
 *   - 中断是一种重要的线程协作机制
 *
 *   - Thread中3个与中断相关的方法
 *     public void interrupt             中断线程, 但实际上是给线程设置中断标记, 并不会使得线程立刻停止执行
 *     public boolean isInterrupted      通过检测线程的线程标记, 判断当前线程是否被中断
 *     public static boolean interrupted 判断当前线程是否被中断, 判断过后将这个线程的中断标记清除
 *
 *   - 其实使用Thread提供的中断机制, 就好像使用我们自己手工添加一个线程结束标记一样,
 *     效果上是差不多的, 但Thread提供的中断机制更加完整, 可以跟wait和sleep配合使用,
 *     所以说轮子已经造好了, 我们就不必再花过多的时间实现中断机制.
 *
 * 2018/5/17 21:22
 *
 * @author plaYwiThsouL
 */
public class ThreadInterruption {
    public static void main(String[] args) throws InterruptedException {
        useInterruptC();
    }

    // 仅仅调用interrupt并不会真正地中断线程, 而只是给线程设置中断标记
    private static void useInterruptA() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
            }
        });
        thread.start();
        Thread.sleep(200);
        // 给目标线程设置中断标志, 相当于通知该线程进行中断
        // 调用interrupt后, 该线程会停止执行吗?
        thread.interrupt();
    }

    // interrupt和is调用isInterrupted判断线程是否中断, 根据判断结果实现我们想要的操作
    private static void useInterruptB() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted!");
                    break;
                }
                Thread.yield();
            }
        });
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }

    // 在调用线程的sleep时调用interrupt中断线程,
    // sleep会抛出InterruptedException, 同时清除该线程的中断标
    private static void useInterruptC() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted!");
                    break;
                }
                try {
                    // 这里进行休眠会由于中断而抛出异常, 同时会清除中断标记,
                    // 如果不在catch字句重新设置中断标记, 下一次循环就不能捕获到这个中断
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted when sleep");
                    // 重新设置中断状态
                    Thread.currentThread().interrupt();
                }
                Thread.yield();
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}