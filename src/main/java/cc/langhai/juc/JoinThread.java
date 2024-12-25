package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * join方法
 *
 * @author langhai.cc
 * @since 2024-12-25
 */
@Slf4j(topic = "c.JoinThread")
public class JoinThread {

    private static int r1 = 0;

    private static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    /**
     * join方法的使用
     */
    public static void test01() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            r1 = 10;
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            r2 = 20;
        });

        long start = System.currentTimeMillis();
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        long end = System.currentTimeMillis();
        log.debug("r1:{}, r2:{}, cost:{}", r1, r2, end - start);
    }
}
