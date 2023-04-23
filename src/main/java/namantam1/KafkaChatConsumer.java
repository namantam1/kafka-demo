package namantam1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class KafkaChatConsumer {

  public static final String BOOTSTRAP_SERVER = "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092";
  public static final String CONSUMER_GROUP_ID = "ndfbtwex-KAFKA_CHAR_CONSUMER-1";

  public static void main(String[] args) {
    // setup config
    Properties config = new Properties();
    config.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    config.put("security.protocol", "SASL_SSL");
    config.put("sasl.mechanism", "PLAIN");
//        config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required " +
//                "username='' password='';");

    // setup consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
    consumer.subscribe(List.of("STREAM_INPUT-1"));

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (var record : records) {
        System.out.printf("key = %s, value = %s%n", record.key(), record.value());
      }
    }
  }
}
