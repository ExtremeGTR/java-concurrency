/**
 * 使用32位虚拟机运行该程序就会出现问题
 *   - 对于32位系统来说, long型整数的操作并不是原子性的(long是64位)
 *
 * 2018/5/10 20:52
 *
 * @author plaYwiThsouL
 */
public class MultiThreadLong {
    public static void main(String[] args) {
        new Thread(new ChangValue(111L)).start();
        new Thread(new ChangValue(-999L)).start();
        new Thread(new ChangValue(333L)).start();
        new Thread(new ChangValue(-444L)).start();
        new Thread(new ReadValue()).start();
    }

    private static long value = 0L;

    private static class ChangValue implements Runnable {
        long to;

        ChangValue(long to) {
            this.to = to;
        }

        @Override
        public void run() {
            while (true) {
                value = to;
                Thread.yield();
            }
        }
    }

    private static class ReadValue implements Runnable {
        @Override
        public void run() {
            while (true) {
                long tmp = value;
                if (tmp != 111L && tmp != -999L && tmp != 333L && tmp != -444L)
                    System.out.println(tmp);
                Thread.yield();
            }
        }
    }
}