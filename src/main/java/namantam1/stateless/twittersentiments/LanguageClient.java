package namantam1.stateless.twittersentiments;

import namantam1.stateless.twittersentiments.models.EntitySentiment;
import namantam1.stateless.twittersentiments.models.Tweet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LanguageClient {
  public static List<EntitySentiment> getEntitySentiment(Tweet tweet) {
    return Arrays.stream(tweet
        .getText()
        .toLowerCase()
        .replace("#", "")
        .split(" "))
      .map(
        word -> EntitySentiment.builder()
          .createdAt(tweet.getCreatedAt())
          .id(tweet.getId())
          .entity(word)
          .text(tweet.getText())
          .salience(randomDouble())
          .sentimentScore(randomDouble())
          .sentimentMagnitude(randomDouble())
          .build()
      )
      .collect(Collectors.toList());
  }

  private static Double randomDouble() {
    return ThreadLocalRandom.current().nextDouble(0, 1);
  }

  public static Tweet translate(Tweet tweet, String targetLang) {
    tweet.setText("translated: " + tweet.getText());
    tweet.setLang(targetLang);
    return tweet;
  }
}
