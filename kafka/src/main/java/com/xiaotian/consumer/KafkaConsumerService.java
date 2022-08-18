package com.xiaotian.consumer;

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

}
