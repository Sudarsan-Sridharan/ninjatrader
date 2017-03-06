package com.bn.ninjatrader.model.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
public class TradeAlgorithm {
  public static final Builder builder() {
    return new Builder();
  }

  private final String id;

  private final String userId;

  private final String algorithm;

  private final String description;

  public TradeAlgorithm(final String id,
                        final String userId,
                        final String algorithm,
                        final String description) {
    checkNotNull(id, "id must not be null.");
    checkNotNull(userId, "userId must not be null.");
    checkNotNull(algorithm, "algorithm must not be null.");

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

    public Builder id(final String id) {
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
