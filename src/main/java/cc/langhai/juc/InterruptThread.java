package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * interrupt线程打断
 *
 * @author langhai.cc
 * @since 2024-12-25
 */
@Slf4j(topic = "c.InterruptThread")
public class InterruptThread {
    public static void main(String[] args) throws InterruptedException {
        test01();
        test02();
    }


    /**
     * 测试 interrupt() 方法，sleep阻塞状态下打断。
     */
    public static void test01() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "thread-01");

        thread.start();
        Thread.sleep(200);
        thread.interrupt();
        Thread.sleep(200);
        log.debug("打断状态：{}", thread.isInterrupted());
    }

    /**
     * 测试 interrupt() 方法，线程正常运行。
     *
     * @throws InterruptedException 打断异常
     */
    public static void test02() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true){
                Thread currentThread = Thread.currentThread();
                boolean interrupted = currentThread.isInterrupted();
                if (interrupted) {
                    log.debug("打断状态：{}", interrupted);
                    break;
                }
            }
        }, "thread-02");
        thread.start();

        Thread.sleep(200);
        thread.interrupt();
    }
}
