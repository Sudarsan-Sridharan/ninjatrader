package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimContextFactory {
  private static final Logger LOG = LoggerFactory.getLogger(SimContextFactory.class);
  private static final int DEFAULT_MAX_HISTORY_SIZE = 52;

  private final PriceDao priceDao;
  private final BrokerFactory brokerFactory;
  private final BoardLotTable boardLotTable;

  @Inject
  public SimContextFactory(final PriceDao priceDao,
                           final BrokerFactory brokerFactory,
                           final BoardLotTable boardLotTable) {
    this.priceDao = priceDao;
    this.brokerFactory = brokerFactory;
    this.boardLotTable = boardLotTable;
  }

  public SimContext create(final SimulationRequest req) {
    final List<Price> priceList = priceDao.findPrices().withSymbol(req.getSymbol())
        .from(req.getFrom()).to(req.getTo()).now();
    final Account account = new Account(new Portfolio(), new Bookkeeper(), new TradeStatistic(), req.getStartingCash());
    final Broker broker = brokerFactory.createBroker();
    final History history = History.withMaxSize(DEFAULT_MAX_HISTORY_SIZE);

    return SimContext.builder().account(account).broker(broker).boardLotTable(boardLotTable).history(history)
        .pricesForSymbol(req.getSymbol(), priceList).build();
  }
}
