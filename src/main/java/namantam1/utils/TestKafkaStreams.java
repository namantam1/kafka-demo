package namantam1.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;

import java.util.List;
import java.util.Properties;

@Slf4j
public class TestKafkaStreams {
  private final TopologyTestDriver testDriver;
  private static final Serde<byte[]> bytesSerde = new Serdes.ByteArraySerde();

  public TestKafkaStreams(Topology topology, Properties config) {
    testDriver = new TopologyTestDriver(topology, config);
  }

  public void start() {
    // nothing to do
  }

  public void close() {
    testDriver.close();
  }

  public <V> void produce(String topic, List<V> msgs, Serializer<V> valueSerializer) {
    TestInputTopic<byte[], V> inputTopic = testDriver.createInputTopic(topic,
      bytesSerde.serializer(), valueSerializer);
    msgs.forEach(value -> {
      inputTopic.pipeInput(value);
      log.info("Produced [{}]: {}", topic, value);
    });
  }

  public <V> void consume(String topic, Deserializer<V> valueDeserializer) {
    TestOutputTopic<byte[], V> outputTopic = testDriver.createOutputTopic(topic,
      bytesSerde.deserializer(), valueDeserializer);
    outputTopic.readValuesToList()
      .forEach(val -> log.info("Consumed [{}]: {}", topic, val));
  }
}
