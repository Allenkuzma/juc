package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * synchronized关键字使用
 *
 * @author langhai.cc
 * @since 2024-12-27
 */
@Slf4j(topic = "c.SynchronizedTest01")
public class SynchronizedTest01 {
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "thread-01");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "thread-02");

        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        log.debug("count: {}", room.getValue());
    }
}

class Room {

    int value = 0;

    public void increment() {
        synchronized (this) {
            value++;
        }
    }

    public void decrement() {
        synchronized (this) {
            value--;
        }
    }

    public int getValue() {
        synchronized (this) {
            return value;
        }
    }

}
