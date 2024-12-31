package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步模式之保护性暂停
 *
 * @author langhai.cc
 * @since 2024-12-31
 */
@Slf4j(topic = "c.ProtectivePause")
public class ProtectivePause {

    public static void main(String[] args) throws IOException {
        GuardedObject guardedObject = new GuardedObject();

        new Thread(() -> {
            // 等待结果
            log.debug("等待结果...");
            List<String> list = (List) guardedObject.get(2000);
            if (list != null) {
                log.debug("结果是：{}", list.size());
            }
        }, "thread-01").start();

        new Thread(() -> {
            log.debug("下载中...");
            try {
                List<String> download = download();
                guardedObject.complete(download);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "thread-02").start();
    }

    public static List<String> download() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL("https://www.baidu.com/").openConnection();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

}

class GuardedObject {

    private Object response;

    public Object get(long millis) {
        synchronized (this) {
            // 记录开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一次循环应该等待时间
                long waitTime = millis - passedTime;
                // 经历时间是否超过最大等待时间
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }

}
