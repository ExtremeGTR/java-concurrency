package com.makwan.concurrency.a_basic;

import java.util.UUID;

/**
 * 使用ThreadLocal
 *   - ThreadLocal提供线程局部变量, 即每个访问该变量的线程都会初始化一个完全独立的变量副本
 *
 *   - 额外问题
 *     如何在一个线程结束的时候做一些工作? 有没有线程结束事件可以给我们注册?
 *
 * 2018/5/19 12:42
 *
 * @author plaYwiThsouL
 */
public class ThreadLocalDemonstration {
    private static ThreadLocal<Integer> map1 = new ThreadLocal<>();
    private static ThreadLocal<User> map2 = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                int ramdomInt = (int) (Math.random() * 100 + 1);
                String randomStr = UUID.randomUUID().toString();
                map1.set(ramdomInt);
                map2.set(new User(randomStr, randomStr, randomStr, ramdomInt));

                printData();
            }).start();
        }
    }

    private static void printData() {
        System.out.println("in " + Thread.currentThread().getName() + "\n"
                + "  integer: " + map1.get() + "\n  user: " + map2.get()
                + "\n  connection: " + ConnectionManager.getConnection() + "\n");
    }

    // 模拟数据库连接管理
    private static class ConnectionManager {
        private static ThreadLocal<MyJdbcConnection> connections = new ThreadLocal<>();

        private ConnectionManager() { }

        private static synchronized MyJdbcConnection getConnection() {
            MyJdbcConnection connection = connections.get();
            if (connection == null) {
                connection = new MyJdbcConnection();
                connections.set(connection);
            }
            return connection;
        }

        private static class MyJdbcConnection { }
    }

    private static class User {
        private String id;
        private String name;
        private String password;
        private int age;

        private User() {
        }

        private User(String id, String name, String password, int age) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", password='" + password + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}