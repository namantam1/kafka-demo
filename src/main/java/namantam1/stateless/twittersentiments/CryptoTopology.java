package namantam1.stateless.twittersentiments;

import lombok.extern.slf4j.Slf4j;
import namantam1.stateless.twittersentiments.models.EntitySentiment;
import namantam1.stateless.twittersentiments.models.Tweet;
import namantam1.utils.JsonSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.List;
import java.util.Map;

@Slf4j
public class CryptoTopology {
  private static final List<String> currencies = List.of("bitcoin", "ethereum");

  // serdes
  public static final Serde<Tweet> tweetSerde = new JsonSerde<>(Tweet.class);
  public static final Serde<EntitySentiment> entitySentimentSerde = new JsonSerde<>(EntitySentiment.class);
  public static final Serde<byte[]> bytesSerde = new Serdes.ByteArraySerde();

  // predicates
  private static final Predicate<byte[], Tweet> englishTweets =
    (key, tweet) -> tweet.getLang().equals("en");
  private static final Predicate<byte[], Tweet> notEnglishTweets =
    (key, tweet) -> !tweet.getLang().equals("en");

  public static Topology build() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<byte[], Tweet> stream = builder
      .stream("tweets", Consumed.with(bytesSerde, tweetSerde));

    stream.foreach((key, val) -> log.info("Topology [tweets-streams]: {}", val));

    KStream<byte[], Tweet> filteredStream = stream
      .filterNot(
        (key, tweet) -> !tweet.getRetweet()
      );

    Map<String, KStream<byte[], Tweet>> branches = filteredStream
      .split(Named.as("b-"))
      .branch(englishTweets, Branched.as("english"))
      .branch(notEnglishTweets, Branched.as("not-english"))
      .noDefaultBranch();

    KStream<byte[], Tweet> englishStream = branches.get("b-english");
    KStream<byte[], Tweet> notEnglishStream = branches.get("b-not-english");

    KStream<byte[], Tweet> translatedStream = notEnglishStream
      .mapValues(
        (tweet) -> LanguageClient.translate(tweet, "en")
      );

    KStream<byte[], Tweet> mergedStream = englishStream.merge(translatedStream);

    KStream<byte[], EntitySentiment> enrichedStream = mergedStream
      .flatMapValues(
        tweet -> {
          List<EntitySentiment> results = LanguageClient.getEntitySentiment(tweet);
          results.removeIf(
            entitySentiment -> !currencies
              .contains(entitySentiment.getEntity())
          );
          return results;
        }
      );

    enrichedStream
      .to("crypto-sentiment", Produced.with(bytesSerde, entitySentimentSerde));


    return builder.build();
  }
}
