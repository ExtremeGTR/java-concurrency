package com.makwan.concurrency.a_basic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * 一个暴力停止线程的方法Thread.stop
 *   - 调用这个方法会直接终止线程, 同时会立即释放该线程所持有的锁,
 *     而这些锁恰恰是用来维持对象的一致性, 所以该方法会破坏数据的一致性
 *
 *   - User数据模拟
 *     这里没有实际数据可用, 同时为了方便测试stop方法带来的问题,
 *     此处约定: 当id和name的数字相同的时候, 那就认定数据是正确的, 反之则认定是错误的
 *     比如: 当id是0并且name是"0"时, 则认为数据是正确的
 *
 *   - 此处开启2个线程:
 *     一个线程读取User数据判断其数据是否正确, 此处简称它为读线程
 *     一个线程向User写入数据, 此处简称它为写线程
 *
 *   - 正常情况下我们所希望的一个流程:
 *     当写线程获得锁在写User数据时, 读线程是无法获得锁进行User数据的读取,
 *     这么说来2个线程操作都是有序完整地进行的:
 *     写线程必须完整地给User对象写入数据, 读线程也是完整地把User对象的数据读取出来
 *     写线程操作时, 读线程必须等待写线程操作完毕, 反之亦然
 *
 *   - 此处破坏数据一致性的流程
 *     开辟写线程对User对象写数据时, 写完id这个属性的数值, 就立马调用stop方法停止写线程,
 *     此刻, id被写入了新数值, 但name还未被写入新数值,
 *     而这个线程词时被stop释放了锁被其他写线程抢去, 所以数据就这么被破坏了,
 *     接着写线程的锁就会被释放掉, 读线程可能会抢到这把锁, 然后读取到这个被破坏的User对象
 *
 *   - 两个休眠时间的设置细节
 *     合理地设置休眠时间可以让stop的问题更快地暴露出来
 *
 *     1.主线程里的休眠时间不能比写线程的休眠时间长太多, 不然主线程还没为写线程调用stop释放锁时,
 *       写线程已经把数据全部写入了, 这并不能暴露stop的问题
 *
 *     2.主线程的休眠时间不能比写线程的休眠时间完全相等甚至是短,
 *       不然你发现stop的问题确实是暴露出来了, 但每次都是看到同样的结果, 这并不能很好地模拟实际情况
 *
 *     其实我们想要的效果就是让写线程刚好写了一点数据的时候, 主线程就立马执行stop, 使得数据被破坏
 *
 *
 * 如果在真实项目中出现该问题, 那麻烦就挺大的,
 * 因为这问题并没有任何异常抛出, 只知道数据错了, 并且这种错误的出现是随机性的,
 * 而有很多种方式都能导致数据错乱, 所以要从很多代码中找到这么隐晦的问题, 即使是经验老道, 那也是非常困难的
 *
 * 2018/5/14 13:41
 *
 * @author plaYwiThsouL
 */
public class UnsafeMethodStop {
    @Test
    public void useMethodStop() throws InterruptedException {
        // 创建并运行一条读线程
        new Thread(new UserReader()).start();

        UserChanger userChanger = new UserChanger();
        // 循环创建写线程并将其运行起来
        while (continueTest) {
            Thread changUserThread = new Thread(userChanger);
            changUserThread.start();
            // 让线程休眠一定的毫秒数模拟调用stop之前还有其他工作要做
            Thread.sleep(250);
            // 调用线程的stop方法
            changUserThread.stop();
        }
    }

    @Test
    public void useStopFlag() throws InterruptedException {
        // 创建并运行一条读线程
        new Thread(new UserReader()).start();

        UserChanger userChanger = new UserChanger();
        // 循环创建写线程并将其运行起来
        while (continueTest) {
            Thread changUserThread = new Thread(userChanger);
            changUserThread.start();
            Thread.sleep(250);
            // 设置自定义的结束标记进行线程退出就不会导致数据错误
            userChanger.stopThread();
        }
    }

    @Before
    public void init() {
        USER.id = "0";
        USER.name = "0";

        continueTest = true;
    }

    @After
    public void finish() {
        USER.id = "0";
        USER.name = "0";
    }

    private static final User USER = new User();
    private static volatile boolean continueTest;

    private static class UserChanger implements Runnable {
        @Override
        public void run() {
            while (continueTest) {
                synchronized (USER) {
                    String value = UUID.randomUUID().toString();
                    USER.id = value;

                    // 这样做更容易把stop问题暴露出来:
                    // 在写入id后稍微停顿一下, 让主线程有机会对执行本任务的线程调用stop
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    USER.name = value;
                }
                Thread.yield();
            }
        }

        private void stopThread() {
            continueTest = false;
        }
    }

    private static class UserReader implements Runnable {
        @Override
        public void run() {
            while (continueTest) {
                synchronized (USER) {
                    if (!USER.id.equals(USER.name)) {
                        System.out.println("****!!! -> " + USER + " <- !!!****");
                        continueTest = false;
                    } else {
                        System.out.println("correct data -> " + USER);
                    }
                }
                Thread.yield();
            }
        }
    }


    // 测试时所用到的User
    private static class User {
        private String id;
        private String name;

        @Override
        public String toString() {
            return "User [id=" + id + ", name=" + name + "]";
        }
    }
}