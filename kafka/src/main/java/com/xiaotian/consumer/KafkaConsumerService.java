package com.xiaotian.consumer;

import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author lichuangbo
 * @date 2022/8/18
 */
@Service
public class KafkaConsumerService {

  @KafkaListener(topics = "PRODUCER_START", groupId = "${spring.kafka.consumer.group-id}")
  public void consumer(@Payload String message) {
    System.out.println(message);
  }

  @KafkaListener(topics = "TOPIC_AUTOCOMMIT", groupId = "${spring.kafka.consumer.group-id}")
  public void consumer2(String message, Consumer<String, String> consumer) {
    System.out.println(message);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // 同步提交
    consumer.commitSync();
  }
}
