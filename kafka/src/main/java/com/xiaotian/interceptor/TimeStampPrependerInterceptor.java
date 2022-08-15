package com.xiaotian.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 时间戳拦截器：在消息体的起始处，添加时间戳
 */
public class TimeStampPrependerInterceptor implements ProducerInterceptor<String, String> {

  // onSend()确保在消息被序列化,计算分区前调用该方法 用户可以在该方法中对消息做任何操作
  // 但最好保证不要修改消息所属的 topic 和分区，否则会影响目标分区的计算（问题排查难度也会加大）
  @Override
  public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
    return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(),
        record.key(),
        System.currentTimeMillis() + "-" + record.value());
  }

  // 会在消息被应答之前或消息发送失败时调用，井且通常都是在 producer 回调逻辑触发之前
  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    // 分区的信息
    System.out.println(metadata.toString());
  }

  // 执行一些资源清理工作
  @Override
  public void close() {
    System.out.println("TimestampPrependerInterceptor关闭");
  }

  @Override
  public void configure(Map<String, ?> configs) {

  }
}
