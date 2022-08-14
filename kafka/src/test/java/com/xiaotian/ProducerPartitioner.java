package com.xiaotian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "partitioner")
public class ProducerPartitioner {

  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;

  /**
   * 看下默认策略  DefaultPartitioner
   * activeProfiles使用默认配置
   */
  @Test
  public void test1() {
    ListenableFuture<SendResult<String, String>> future7 = kafkaTemplate.send("topic-auditPartition", 0, "key1", "record1");
    ListenableFuture<SendResult<String, String>> future8 = kafkaTemplate.send("topic-auditPartition", 1, "key2", "record2");
    ListenableFuture<SendResult<String, String>> future9 = kafkaTemplate.send("topic-auditPartition", 2, "key3", "record3");
    // 指定的分区不存在时，会报错
//    ListenableFuture<SendResult<String, String>> future10 = kafkaTemplate.send("topic-auditPartition", 3, "key4", "record4");
    try {
      System.out.println("指定partition的分区策略：向指定partition发送消息");
      System.out.println(future7.get());
      System.out.println(future8.get());
      System.out.println(future9.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    ListenableFuture<SendResult<String, String>> future4 = kafkaTemplate.send("topic-auditPartition", "audit", "record1");
    ListenableFuture<SendResult<String, String>> future5 = kafkaTemplate.send("topic-auditPartition", "audit", "record2");
    ListenableFuture<SendResult<String, String>> future6 = kafkaTemplate.send("topic-auditPartition", "audit111", "record3");
    try {
      System.out.println("没有partition但指定相同key的分区策略：按照key进行哈希运算，得到分区值");
      System.out.println(future4.get());
      System.out.println(future5.get());
      System.out.println(future6.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send("topic-auditPartition", "record1");
    ListenableFuture<SendResult<String, String>> future2 = kafkaTemplate.send("topic-auditPartition", "record2");
    ListenableFuture<SendResult<String, String>> future3 = kafkaTemplate.send("topic-auditPartition", "record3");
    try {
      System.out.println("没有partition也没有key的分区策略：选择在批处理满时更改的分区");
      System.out.println(future1.get());
      System.out.println(future2.get());
      System.out.println(future3.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * 自定义分区策略
   * activeProfiles=partitioner
   */
  @Test
  public void test2() {
    ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send("topic-auditPartition", "non-key record");
    ListenableFuture<SendResult<String, String>> future2 = kafkaTemplate.send("topic-auditPartition", "non-key record");
    ListenableFuture<SendResult<String, String>> future3 = kafkaTemplate.send("topic-auditPartition", "other", "non-audit record");

    ListenableFuture<SendResult<String, String>> future4 = kafkaTemplate.send("topic-auditPartition", "audit", "audit record");

    try {
      System.out.println(future1.get());
      System.out.println(future2.get());
      System.out.println(future3.get());
      System.out.println(future4.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
