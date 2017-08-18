package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.model.Algorithm;

import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public interface AlgorithmDao {

  Algorithm save(final Algorithm algorithm);

  FindAlgorithmsOperation findAlgorithms();

  Optional<Algorithm> findOneByAlgorithmId(final String algoId);

  void delete(final String algoId);

  void delete(final String userId, final String algoId);

  /**
   * Builder interface for finding Algorithms
   */
  interface FindAlgorithmsOperation {

    FindAlgorithmsOperation withUserId(final String userId);

    FindAlgorithmsOperation isAutoScan(final boolean isAutoScan);

    List<Algorithm> now();
  }
}
