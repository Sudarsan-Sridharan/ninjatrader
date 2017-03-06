package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoTradeAlgorithmDocument {

  public static final MongoTradeAlgorithmDocument copyFrom(final TradeAlgorithm algo) {
    checkNotNull(algo, "algo must not be null.");
    return new MongoTradeAlgorithmDocument(algo.getId(), algo.getUserId(), algo.getDescription(), algo.getAlgorithm());
  }

  @MongoId
  @MongoObjectId
  private String mongoId;

  @JsonProperty("algoId")
  private String tradeAlgorithmId;

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("desc")
  private String description;

  @JsonProperty("algo")
  private String tradeAlgorithm;

  public MongoTradeAlgorithmDocument(@JsonProperty("algoId") final String tradeAlgorithmId,
                                     @JsonProperty("userId") final String userId,
                                     @JsonProperty("desc") final String description,
                                     @JsonProperty("algo") final String tradeAlgorithm) {
    this.tradeAlgorithmId = tradeAlgorithmId;
    this.userId = userId;
    this.description = description;
    this.tradeAlgorithm = tradeAlgorithm;
  }

  public String getTradeAlgorithmId() {
    return tradeAlgorithmId;
  }

  public void setTradeAlgorithmId(String tradeAlgorithmId) {
    this.tradeAlgorithmId = tradeAlgorithmId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTradeAlgorithm() {
    return tradeAlgorithm;
  }

  public void setTradeAlgorithm(String tradeAlgorithm) {
    this.tradeAlgorithm = tradeAlgorithm;
  }

  public TradeAlgorithm toTradeAlgorithm() {
    return TradeAlgorithm.builder()
        .id(tradeAlgorithmId)
        .userId(userId)
        .description(description)
        .algorithm(tradeAlgorithm)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MongoTradeAlgorithmDocument that = (MongoTradeAlgorithmDocument) o;
    return Objects.equal(tradeAlgorithmId, that.tradeAlgorithmId) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(description, that.description) &&
        Objects.equal(tradeAlgorithm, that.tradeAlgorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tradeAlgorithmId, userId, description, tradeAlgorithm);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("tradeAlgorithmId", tradeAlgorithmId)
        .add("userId", userId)
        .add("description", description)
        .add("tradeAlgorithm", tradeAlgorithm)
        .toString();
  }
}
