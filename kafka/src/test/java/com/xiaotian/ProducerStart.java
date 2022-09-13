package com.xiaotian;

import javax.annotation.Resource;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

/**
 * 生产者发送消息 注意：kafka底层发送消息都是异步的，只不过在Java api层面进一步区分出了同步和异步
 *
 * @author lichuangbo
 * @date 2022/8/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerStart {

  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;

  /**
   * 最简单的异步发送消息
   */
  @Test
  public void test1() {
    kafkaTemplate.send("PRODUCER_START", "producer: hello kafka");
  }

  /**
   * 同步发送消息
   */
  @Test
  public void test2() {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate
        .send("PRODUCER_START", "producer: hello kafka");
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * 异步发送消息，kafkaTemplate添加一个监听器
   */
  @Test
  public void test3() {
    kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
      @Override
      public void onSuccess(ProducerRecord<String, String> producerRecord,
          RecordMetadata recordMetadata) {
        System.out.println("消息发送成功：" + producerRecord.toString());
      }

      @Override
      public void onError(ProducerRecord<String, String> producerRecord,
          RecordMetadata recordMetadata, Exception exception) {
        System.out.println("消息发送失败：" + producerRecord.toString());
      }
    });
    kafkaTemplate.send("PRODUCER_START", "producer: hello kafka");
  }

  /**
   * 异步发送消息，future添加一个回调函数
   */
  @Test
  public void test4() {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate
        .send("PRODUCER_START", "producer: hello kafka");
    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        System.out.println("消息发送失败：" + throwable.toString());
      }

      @Override
      public void onSuccess(SendResult<String, String> stringStringSendResult) {
        System.out.println("消息发送成功：" + stringStringSendResult.getProducerRecord().toString());
      }
    });
  }

  /**
   * 循环发送消息
   */
  @Test
  public void test5() {
    for (int i = 0; i < 10; i++) {
      kafkaTemplate.send("TOPIC_AUTOCOMMIT", "producer" + i + ": hello kafka");
    }
  }
}
