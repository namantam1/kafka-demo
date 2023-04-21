package namantam1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ChatProducer {
  public static final String BOOTSTRAP_SERVER = "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092";

  public static void main(String[] args) {
    // setup config
    Properties config = new Properties();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put("security.protocol", "SASL_SSL");
    config.put("sasl.mechanism", "PLAIN");

    // setup producer
    KafkaProducer<String, String> producer = new KafkaProducer<>(config);

    producer.send(new ProducerRecord<>("chat-topic", "hi!!!"));

    producer.close();
  }
}
