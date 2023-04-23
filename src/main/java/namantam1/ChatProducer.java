package namantam1;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import namantam1.utils.FileUtil;
import namantam1.utils.JsonUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
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

    var data = FileUtil.readJsonAsList("C:/Users/Naman Tamrakar/Downloads/user_data.json",
      JsonNode.class);
    int partition = 0;
    for (var el: data) {
        var val = JsonUtil.toJson(el);
        producer.send(new ProducerRecord<>("STREAM_INPUT-1", partition % 3, null, val));
        log.info("published: {}", val);
        partition++;
    }

    producer.close();
  }
}
