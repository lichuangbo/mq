package com.xiaotian.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * counter拦截器：记录发送成功和失败的消息数目
 */
public class CounterInterceptor implements ProducerInterceptor<String, String> {

  private int successCount;
  private int errorCount;

  @Override
  public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
    System.out.println("counterInterceptor接收到的消息： " + record);
    return record;
  }

  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    if (exception == null) {
      successCount++;
    } else {
      errorCount++;
    }
  }

  @Override
  public void close() {
    System.out.println("countInterceptor记录的消息发送成功和失败的数目：");
    System.out.println("success :" + successCount);
    System.out.println("error :" + errorCount);
  }

  @Override
  public void configure(Map<String, ?> configs) {

  }
}
