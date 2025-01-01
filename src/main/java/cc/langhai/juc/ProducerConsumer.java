package cc.langhai.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 异步模式 生产者/消费者
 *
 * @author langhai.cc
 * @since 2025-01-01
 */
@Slf4j(topic = "c.ProducerConsumer")
public class ProducerConsumer {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id, "message-" + id));
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Message message = queue.take();
            }
        }, "消费者").start();
    }
}

final class Message {

    private int id;

    private String message;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}

@Slf4j(topic = "c.MessageQueue")
class MessageQueue {

    private LinkedList<Message> queue;

    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    log.debug("队列已空，消费者等待...");
                    queue.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Message message = queue.removeFirst();
            log.debug("消费了消息：{}", message);
            queue.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() == capacity) {
                log.debug("队列已满，生产者等待...");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.addLast(message);
            log.debug("生产了消息：{}", message);
            queue.notifyAll();
        }
    }

}
