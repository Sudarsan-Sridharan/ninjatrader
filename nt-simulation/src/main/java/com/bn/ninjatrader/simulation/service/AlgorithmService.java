package com.bn.ninjatrader.simulation.service;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.simulation.exception.AlgorithmIdNotFoundException;
import com.bn.ninjatrader.simulation.script.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.script.AlgorithmScript;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AlgorithmService {

  private final AlgorithmDao tradeAlgorithmDao;
  private final AlgorithmScriptFactory scriptFactory;

  @Inject
  public AlgorithmService(final AlgorithmDao tradeAlgorithmDao,
                          final AlgorithmScriptFactory scriptFactory) {
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.scriptFactory = scriptFactory;
  }

  /**
   * Find SimTradeAlgorithm given its ID.
   * Data is stored as json, so need to convert it to object.
   */
  public AlgorithmScript findById(final String algoId) {
    final TradeAlgorithm algorithm = tradeAlgorithmDao.findByTradeAlgorithmId(algoId)
        .orElseThrow(() -> new AlgorithmIdNotFoundException(algoId));

    final AlgorithmScript script = scriptFactory.create(algorithm);

    return script;
  }
}
