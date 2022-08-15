package com.xiaotian;

import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "interceptor")
public class ProducerInterceptor {

  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void test1() {
    ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate
        .send("topic-interceptor", 0, "key1", "record1");
    ListenableFuture<SendResult<String, String>> future2 = kafkaTemplate
        .send("topic-interceptor", 1, "key2", "record2");
    ListenableFuture<SendResult<String, String>> future3 = kafkaTemplate
        .send("topic-interceptor", 2, "key3", "record3");
    try {
      System.out.println(future1.get());
      System.out.println(future2.get());
      System.out.println(future3.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
