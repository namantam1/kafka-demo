package namantam1.stateless.twittersentiments;

import lombok.extern.slf4j.Slf4j;
import namantam1.stateless.twittersentiments.models.Tweet;
import namantam1.utils.FileUtil;
import namantam1.utils.TestKafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

import static namantam1.stateless.twittersentiments.CryptoTopology.entitySentimentSerde;
import static namantam1.stateless.twittersentiments.CryptoTopology.tweetSerde;

@Slf4j
public class TwitterStream {

  public static void main(String[] args) {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev");
//    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");

    Topology topology = CryptoTopology.build();

    var streams = new TestKafkaStreams(topology, config);
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    log.info("Starting Twitter streams");
    streams.start();


    streams.produce("tweets",
      FileUtil.readJsonAsList("data/tweets.json", Tweet.class), tweetSerde.serializer());
    streams.consume("crypto-sentiment", entitySentimentSerde.deserializer());
//    streams.consume("crypto-sentiment", new StringDeserializer());
  }

}
