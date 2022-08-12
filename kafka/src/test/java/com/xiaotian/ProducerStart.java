package com.xiaotian;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 生产者最简单的发送消息
 * @author lichuangbo
 * @date 2022/8/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerStart {

  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void test() {
    kafkaTemplate.send("PRODUCER_START", "producer: hello kafka");
  }
}
