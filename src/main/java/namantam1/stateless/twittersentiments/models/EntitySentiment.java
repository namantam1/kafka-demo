package namantam1.stateless.twittersentiments.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntitySentiment {
  @JsonAlias("CreatedAt")
  private Long createdAt;
  @JsonAlias("Id")
  private Long id;
  @JsonAlias("Entity")
  private String entity;
  @JsonAlias("Text")
  private String text;
  @JsonAlias("SentimentScore")
  private Double sentimentScore;
  @JsonAlias("SentimentMagnitude")
  private Double sentimentMagnitude;
  @JsonAlias("Salience")
  private Double salience;
}
