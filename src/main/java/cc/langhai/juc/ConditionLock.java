package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock多个条件变量
 *
 * @author langhai.cc
 * @since 2025-01-03
 */
@Slf4j(topic = "c.ConditionLock")
public class ConditionLock {

    static ReentrantLock room = new ReentrantLock();

    static Condition waitCigaretteCondition = room.newCondition();

    static Condition waitBreakfastCondition = room.newCondition();

    static boolean hasCigarette = false;

    static boolean hasBreakfast = false;

    public static void main(String[] args) {

        new Thread(() -> {
            room.lock();
            try {
                log.debug("有烟没？{}", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        waitCigaretteCondition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.debug("有烟没？{}", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活！");
                }
            } finally {
                room.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            room.lock();
            try {
                log.debug("外卖送到没？{}", hasBreakfast);
                while (!hasBreakfast) {
                    log.debug("没外卖，先歇会！");
                    try {
                        waitBreakfastCondition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.debug("外卖送到没？{}", hasBreakfast);
                if (hasBreakfast) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没外卖，先歇会！");
                }
            } finally {
                room.unlock();
            }
        }, "小女").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            room.lock();
            try {
                hasCigarette = true;
                waitCigaretteCondition.signal();
            } finally {
                room.unlock();
            }
        }, "送烟的").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            room.lock();
            try {
                hasBreakfast = true;
                waitBreakfastCondition.signal();
            } finally {
                room.unlock();
            }
        }, "送外卖的").start();
    }
}
