package com.xiaotian.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 在生产者发送消息时，需要确定将消息发送到指定topic 的哪个分区上
 * 默认策略：相同key的所有消息发送到相同的分区上；如果没有给消息指定key，采用轮询的方式将消息均匀的分配到所有分区上（并不是轮训，3.0中采用的是粘性策略，
 * 当创建一个新的批处理时，将选择一个新的分区）
 * 也可以自定义partitioner 来完成特定的业务
 */
public class AuditPartitioner implements Partitioner {
  private Random random;

  // 手动实现Kafka topic分区索引算法
  @Override
  public int partition(String topic, Object keyObj, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    String key = (String) keyObj;
    List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
    int size = partitionInfos.size();
    int auditPartition = size - 1;
    // 空键或者键不包含 audit 关键词时，随机分配到前n-1个分区中； 否则分配到最后一个分区中
    if (key == null || !key.contains("audit")) {
      int randomPartition = random.nextInt(size - 1);
      System.out.println("randomPartition=" + randomPartition);
      return randomPartition;
    } else {
      return auditPartition;
    }
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {
    random = new Random();
  }
}
