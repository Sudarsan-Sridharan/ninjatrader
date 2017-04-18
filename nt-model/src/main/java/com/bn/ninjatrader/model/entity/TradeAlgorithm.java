package com.bn.ninjatrader.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeAlgorithm {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("algorithmId")
  private final String id;

  @JsonProperty("userId")
  private final String userId;

  @JsonProperty("algorithm")
  private final String algorithm;

  @JsonProperty("description")
  private final String description;

  public TradeAlgorithm(@JsonProperty("algorithmId") final String id,
                        @JsonProperty("userId") final String userId,
                        @JsonProperty("algorithm") final String algorithm,
                        @JsonProperty("description") final String description) {
    this.id = id;
    this.userId = userId;
    this.algorithm = algorithm;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TradeAlgorithm that = (TradeAlgorithm) o;
    return Objects.equal(id, that.id) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(algorithm, that.algorithm) &&
        Objects.equal(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, userId, algorithm, description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("userId", userId)
        .add("algorithm", algorithm)
        .add("description", description)
        .toString();
  }

  /**
   * Builder
   */
  public static final class Builder {
    private String id;
    private String userId;
    private String algorithm;
    private String description;

    public Builder algoId(final String id) {
      this.id = id;
      return this;
    }

    public Builder userId(final String userId) {
      this.userId = userId;
      return this;
    }

    public Builder algorithm(final String algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public Builder description(final String description) {
      this.description = description;
      return this;
    }

    public TradeAlgorithm build() {
      return new TradeAlgorithm(id, userId, algorithm, description);
    }
  }
}
