package com.bn.ninjatrader.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class CreateTradeAlgorithmRequest {
  private static final Logger LOG = LoggerFactory.getLogger(CreateTradeAlgorithmRequest.class);

  @JsonProperty("algorithm")
  private String algorithm;

  @JsonProperty("description")
  private String description;

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateTradeAlgorithmRequest that = (CreateTradeAlgorithmRequest) o;
    return Objects.equal(algorithm, that.algorithm) &&
        Objects.equal(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(algorithm, description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("algorithm", algorithm)
        .add("description", description)
        .toString();
  }
}
