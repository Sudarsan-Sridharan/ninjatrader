package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;

import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public interface TradeAlgorithmDao {

  void save(final TradeAlgorithm tradeAlgorithm);

  List<TradeAlgorithm> findByUserId(final String userId);

  Optional<TradeAlgorithm> findByTradeAlgorithmId(final String algoId);
}
