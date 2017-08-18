package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.common.model.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MongoAlgorithmDocument {
  private static final Logger LOG = LoggerFactory.getLogger(MongoAlgorithmDocument.class);

  public static final MongoAlgorithmDocument copyFrom(final Algorithm algo) {
    checkNotNull(algo, "algo must not be null.");
    return new MongoAlgorithmDocument(algo.getId(), algo.getUserId(), algo.getDescription(), algo.getAlgorithm(), algo.isAutoScan());
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

  @JsonProperty("isAutoScan")
  private boolean isAutoScan;

  public MongoAlgorithmDocument(@JsonProperty("algoId") final String algorithmId,
                                @JsonProperty("userId") final String userId,
                                @JsonProperty("desc") final String description,
                                @JsonProperty("algo") final String algorithm,
                                @JsonProperty("isAutoScan") final boolean isAutoScan) {
    this.algorithmId = algorithmId;
    this.userId = userId;
    this.description = description;
    this.algorithm = algorithm;
    this.isAutoScan = isAutoScan;
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

  @JsonIgnore
  public boolean isAutoScan() {
    return isAutoScan;
  }

  public void setAutoScan(boolean autoScan) {
    isAutoScan = autoScan;
  }

  public Algorithm toTradeAlgorithm() {
    return Algorithm.builder()
        .algoId(algorithmId)
        .userId(userId)
        .description(description)
        .algorithm(algorithm)
        .isAutoScan(isAutoScan)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MongoAlgorithmDocument that = (MongoAlgorithmDocument) o;
    return isAutoScan == that.isAutoScan &&
        Objects.equal(mongoId, that.mongoId) &&
        Objects.equal(algorithmId, that.algorithmId) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(description, that.description) &&
        Objects.equal(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mongoId, algorithmId, userId, description, algorithm, isAutoScan);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("mongoId", mongoId)
        .add("algorithmId", algorithmId)
        .add("userId", userId)
        .add("description", description)
        .add("algorithm", algorithm)
        .add("isAutoScan", isAutoScan)
        .toString();
  }
}
