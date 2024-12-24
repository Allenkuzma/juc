package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程方式
 *
 * @author langhai.cc
 * @since 2024-12-23
 */
@Slf4j(topic = "c.CreateThread")
public class CreateThread {
    public static void main(String[] args) {
        FutureTask<Integer> integerFutureTask = test03();
        try {
            Integer integer = integerFutureTask.get();
            log.debug("计算结果：{}", integer);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.debug("main running...");
    }

    /**
     * 创建线程第一种方式：继承Thread类
     */
    public static void test01(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        thread.setName("test01");
        thread.start();
    }

    /**
     * 创建线程第二种方式：使用Runnable接口
     */
    public static void test02(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        Thread thread = new Thread(runnable, "test02");
        thread.start();

        // lambda形式
        Thread threadLambda = new Thread(() -> {
            log.debug("running...");
        }, "test02Lambda");
        threadLambda.start();
    }

    /**
     * 创建线程第三种方式：使用Callable接口
     */
    public static FutureTask<Integer> test03() {
        FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> {
            log.debug("callable 计算中...");
            Thread.sleep(2000);
            return 100;
        });

        Thread thread = new Thread(integerFutureTask, "test03");
        thread.start();

        return integerFutureTask;
    }
}
