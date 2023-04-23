package namantam1.stateless.twittersentiments.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Tweet {
  @JsonAlias("CreatedAt")
  private Long createdAt;
  @JsonAlias("Id")
  private Long id;
  @JsonAlias("Lang")
  private String lang;
  @JsonAlias("Retweet")
  private Boolean retweet;
  @JsonAlias("Text")
  private String text;
}
