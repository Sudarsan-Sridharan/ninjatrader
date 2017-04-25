package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.entity.Algorithm;

import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public interface AlgorithmDao {

  Algorithm save(final Algorithm algorithm);

  List<Algorithm> findByUserId(final String userId);

  Optional<Algorithm> findByAlgorithmId(final String algoId);

  void delete(final String algoId);
}
