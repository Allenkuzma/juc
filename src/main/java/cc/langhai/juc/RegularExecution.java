package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 定期执行任务
 *
 * @author langhai.cc
 * @since 2025-01-12
 */
@Slf4j(topic = "c.RegularExecution")
public class RegularExecution {
    public static void main(String[] args) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取本周周日13:50:00.000
        LocalDateTime sunday = now.with(DayOfWeek.SUNDAY).withHour(13).withMinute(50).withSecond(0).withNano(0);
        // 当前时间是否超过本周周日13:50:00.000
        if (now.compareTo(sunday) > 0) {
            sunday = sunday.plusWeeks(1);
        }
        long initialDelay = Duration.between(now, sunday).toMillis();
        long nowWeek = 7 * 24 * 60 * 60 * 1000;

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        log.debug("开始时间：" + new Date());
        executorService.scheduleAtFixedRate(() -> {
            log.debug("执行时间：" + new Date());
        }, initialDelay, nowWeek, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
