package com.bn.ninjatrader.model.datastore.document;

import com.bn.ninjatrader.common.model.Algorithm;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
public class AlgorithmDocument {

  public static final AlgorithmDocument copyFrom(final Algorithm algorithm) {
    return new AlgorithmDocument(algorithm);
  }

  @Id
  private String id;

  @Index
  private String userId;

  @Unindex
  private String description;

  @Unindex
  private String algorithm;

  @Index
  private boolean isAutoScan;

  private AlgorithmDocument() {} // Required by objectify

  private AlgorithmDocument(final Algorithm algorithm) {
    this.id = algorithm.getId();
    this.userId = algorithm.getUserId();
    this.description = algorithm.getDescription();
    this.algorithm = algorithm.getAlgorithm();
    this.isAutoScan = algorithm.isAutoScan();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Algorithm toAlgorithm() {
    return Algorithm.builder()
        .algoId(id)
        .userId(userId)
        .description(description)
        .algorithm(algorithm)
        .isAutoScan(isAutoScan)
        .build();
  }
}
