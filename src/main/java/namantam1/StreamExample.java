package namantam1;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import namantam1.utils.JsonUtil;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static namantam1.utils.JsonUtil.*;

@Log4j2
public class StreamExample {
  public static final String BOOTSTRAP_SERVER = "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092";
  public static final String STREAMS_APP_ID = "ndfbtwex-KAFKA_STREAMS_APP-1";
  public static final String INPUT_TOPIC = "STREAM_INPUT-1";
  public static final String OUTPUT_TOPIC = "STREAM_OUTPUT-1";

  public static void main(String[] args) {
    // setup streams config
    Properties config = new Properties();
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, STREAMS_APP_ID);
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
    config.put(StreamsConfig.POLL_MS_CONFIG, 100);
    config.put(StreamsConfig.STATE_DIR_CONFIG, "state" + System.getProperty("id", ""));
    config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
    config.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
    config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

    StreamsBuilder builder = new StreamsBuilder();

    var res = builder
      .<String, String>stream(INPUT_TOPIC)
      .peek((key, val) -> log.info("input: {{}: {}}", key, val))
      .mapValues(JsonUtil::parseJson)
      .groupBy(
        (key, jsonData) -> jsonData.get("gender").asText(),
        Grouped.with(Serdes.String(), new JsonSerde())
      )
      .aggregate(
        () -> 0.0,
        (key, jsonData, aggregatedVal) -> jsonData.get("salary").asDouble() + aggregatedVal,
        Materialized
          .<String, Double, KeyValueStore<Bytes, byte[]>>as("salary-aggregated")
          .withValueSerde(new Serdes.DoubleSerde())
      )
      .toStream()
      .peek((key, val) -> log.info("aggregated: {{}: {}}", key, val));
//      .peek((key, val) -> System.out.printf("Output: {key: %s, value: %s}%n", key, val))
//      .to(OUTPUT_TOPIC);



    Topology topology = builder.build();
    KafkaStreams streams = new KafkaStreams(topology, config);
    streams.cleanUp();
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  private static class JsonSerde implements Serde<JsonNode> {
    @Override
    public Serializer<JsonNode> serializer() {
      return (topic, data) -> toJsonBytes(data);
    }

    @Override
    public Deserializer<JsonNode> deserializer() {
      return (topic, data) -> parseJson(new String(data, StandardCharsets.UTF_8));
    }
  }
}
