package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.model.entity.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoTradeAlgorithmDocument {
  private static final Logger LOG = LoggerFactory.getLogger(MongoTradeAlgorithmDocument.class);

  public static final MongoTradeAlgorithmDocument copyFrom(final Algorithm algo) {
    checkNotNull(algo, "algo must not be null.");
    return new MongoTradeAlgorithmDocument(algo.getId(), algo.getUserId(), algo.getDescription(), algo.getAlgorithm());
  }

  @MongoId
  @MongoObjectId
  private String mongoId;

  @JsonProperty("algoId")
  private String algorithmId;

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("desc")
  private String description;

  @JsonProperty("algo")
  private String algorithm;

  public MongoTradeAlgorithmDocument(@JsonProperty("algoId") final String algorithmId,
                                     @JsonProperty("userId") final String userId,
                                     @JsonProperty("desc") final String description,
                                     @JsonProperty("algo") final String algorithm) {
    this.algorithmId = algorithmId;
    this.userId = userId;
    this.description = description;
    this.algorithm = algorithm;
  }

  public String getAlgorithmId() {
    return algorithmId;
  }

  public void setAlgorithmId(String algorithmId) {
    this.algorithmId = algorithmId;
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

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public Algorithm toTradeAlgorithm() {
    return Algorithm.builder()
        .algoId(algorithmId)
        .userId(userId)
        .description(description)
        .algorithm(algorithm)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MongoTradeAlgorithmDocument that = (MongoTradeAlgorithmDocument) o;
    return Objects.equal(algorithmId, that.algorithmId) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(description, that.description) &&
        Objects.equal(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(algorithmId, userId, description, algorithm);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("algorithmId", algorithmId)
        .add("userId", userId)
        .add("desc", description)
        .add("algo", algorithm)
        .toString();
  }
}
