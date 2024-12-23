package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程方式
 *
 * @author langhai.cc
 * @since 2024-12-23
 */
@Slf4j(topic = "c.CreateThread")
public class CreateThread {
    public static void main(String[] args) {
        test02();
        log.debug("running...");
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
    }
}
