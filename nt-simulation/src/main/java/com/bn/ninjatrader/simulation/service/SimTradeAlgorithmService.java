package com.bn.ninjatrader.simulation.service;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.entity.TradeAlgorithmFactory;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;
import com.bn.ninjatrader.simulation.exception.TradeAlgorithmIdNotFoundException;
import com.bn.ninjatrader.simulation.jackson.SimObjectMapperProvider;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimTradeAlgorithmService {

  private final TradeAlgorithmDao tradeAlgorithmDao;
  private final TradeAlgorithmFactory tradeAlgorithmFactory;
  private final ObjectMapper om;

  @Inject
  public SimTradeAlgorithmService(final TradeAlgorithmDao tradeAlgorithmDao,
                                  final TradeAlgorithmFactory tradeAlgorithmFactory,
                                  final SimObjectMapperProvider simObjectMapperProvider) {
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.tradeAlgorithmFactory = tradeAlgorithmFactory;
    this.om = simObjectMapperProvider.get();
  }

  /**
   * Find SimTradeAlgorithm given its ID.
   * Data is stored as json, so need to convert it to object.
   */
  public SimTradeAlgorithm findById(final String algoId) {
    final Optional<TradeAlgorithm> found =
        tradeAlgorithmDao.findOne(FindTradeAlgorithmRequest.withTradeAlgorithmId(algoId));

    if (!found.isPresent()) {
      throw new TradeAlgorithmIdNotFoundException(algoId);
    }

    // Read algorithm from json
    try {
      return om.readValue(found.get().getAlgorithm(), SimTradeAlgorithm.class);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TradeAlgorithm save(final SaveSimTradeAlgoRequest req) {
    try {
      // Convert algorithm to json
      final String jsonAlgo = om.writeValueAsString(req.getSimTradeAlgorithm());

      // Create algoId if not set
      final TradeAlgorithm.Builder builder;
      if (StringUtils.isEmpty(req.getTradeAlgorithmId())) {
        builder = tradeAlgorithmFactory.create();
      } else {
        builder = TradeAlgorithm.builder().id(req.getTradeAlgorithmId());
      }

      // Create TradeAlgorithm entity
      final TradeAlgorithm tradeAlgorithm = builder
          .userId(req.getUserId()).algorithm(jsonAlgo).description(req.getDescription()).build();

      // Save
      tradeAlgorithmDao.save(SaveTradeAlgorithmRequest.addEntity(tradeAlgorithm));

      return tradeAlgorithm;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
