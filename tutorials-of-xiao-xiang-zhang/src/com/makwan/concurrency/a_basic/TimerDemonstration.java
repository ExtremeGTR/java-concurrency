package com.makwan.concurrency.a_basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器
 *
 * 2018/5/8 20:45
 *
 * @author plaYwiThsouL
 */
public class TimerDemonstration {
    public static void main(String[] args) throws InterruptedException {
        f2();
        while (true) {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Thread.sleep(1000);
        }
    }

    /* 最普通的Timer.schedule方法
     *   - 接受两个参数: TimerTask, 延迟毫秒数
     *   - 从当前时间开始计算, 延迟指定毫秒数后定时器就会执行TimerTask的代码
     */
    private static void f1() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Boom!");
            }
        }, 3000);
    }

    // 定时器中嵌套定时器
    private static void f2() {
        new Timer().schedule(new TimeBomb(), 2000);
    }

    private static class TimeBomb extends TimerTask {
        private static boolean flag = false;

        @Override
        public void run() {
            System.out.println("Boom!!!");
            new Timer().schedule(new TimeBomb(), (flag ^= true) ? 4000 : 2000);
        }
    }
}