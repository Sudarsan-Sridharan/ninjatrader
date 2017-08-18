package com.bn.ninjatrader.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class CreateAlgorithmRequest {
  private static final Logger LOG = LoggerFactory.getLogger(CreateAlgorithmRequest.class);

  @JsonProperty("algoId")
  private String algoId;

  @JsonProperty("algorithm")
  private String algorithm;

  @JsonProperty("description")
  private String description;

  @JsonProperty("isAutoScan")
  private boolean isAutoScan;

  public String getAlgoId() {
    return algoId;
  }

  public void setAlgoId(String algoId) {
    this.algoId = algoId;
  }

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

  public boolean isAutoScan() {
    return isAutoScan;
  }

  public void setAutoScan(boolean autoScan) {
    isAutoScan = autoScan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateAlgorithmRequest that = (CreateAlgorithmRequest) o;
    return isAutoScan == that.isAutoScan &&
        Objects.equal(algoId, that.algoId) &&
        Objects.equal(algorithm, that.algorithm) &&
        Objects.equal(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(algoId, algorithm, description, isAutoScan);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("algoId", algoId)
        .add("description", description)
        .add("isAutoScan", isAutoScan)
        .add("algorithm", algorithm)
        .toString();
  }
}
