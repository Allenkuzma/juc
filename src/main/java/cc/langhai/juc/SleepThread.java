package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread.sleep方法
 *
 * @author langhai.cc
 * @since 2024-12-25
 */
@Slf4j(topic = "c.SleepThread")
public class SleepThread {
    public static void main(String[] args) {
        Thread thread = new Thread("thread-sleep-001") {
            @Override
            public void run() {
                log.debug("thread-sleep-001 running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.debug("sleep 中断");
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("main running...");
        thread.interrupt();
    }
}
