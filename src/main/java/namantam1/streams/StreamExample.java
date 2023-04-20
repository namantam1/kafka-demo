package namantam1.streams;

import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

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
        config.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        StreamsBuilder builder = new StreamsBuilder();

        builder
                .<String, String>stream(INPUT_TOPIC)
                .peek((key, val) -> System.out.printf("Input: {key: %s, value: %s}%n", key, val))
                .filter((key, val) -> val.length() >= 1)
                .peek((key, val) -> System.out.printf("Output: {key: %s, value: %s}%n", key, val))
                .to(OUTPUT_TOPIC);


        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, config);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}
