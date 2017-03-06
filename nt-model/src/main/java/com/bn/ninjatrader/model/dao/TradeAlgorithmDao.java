package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public interface TradeAlgorithmDao {

  List<TradeAlgorithm> save(final SaveTradeAlgorithmRequest request);

  List<TradeAlgorithm> find(final FindTradeAlgorithmRequest request);

  Optional<TradeAlgorithm> findOne(final FindTradeAlgorithmRequest request);
}
