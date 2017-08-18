package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.simulation.exception.AlgorithmNotFoundException;
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
    final Algorithm algorithm = tradeAlgorithmDao.findOneByAlgorithmId(algoId)
        .orElseThrow(() -> new AlgorithmNotFoundException(algoId));

    final AlgorithmScript script = scriptFactory.create(algorithm);

    return script;
  }
}
