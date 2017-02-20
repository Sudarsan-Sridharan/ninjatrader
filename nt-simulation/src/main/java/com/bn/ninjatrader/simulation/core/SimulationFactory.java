package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.calculator.VarCalculatorFactory;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.model.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class SimulationFactory {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactory.class);

  private final PriceDao priceDao;
  private final BrokerFactory brokerFactory;
  private final BarDataFactory barDataFactory;
  private final BoardLotTable boardLotTable;
  private final VarCalculatorFactory varCalculatorFactory;

  @Inject
  public SimulationFactory(final VarCalculatorFactory varCalculatorFactory,
                           final PriceDao priceDao,
                           final BrokerFactory brokerFactory,
                           final BarDataFactory barDataFactory,
                           final BoardLotTable boardLotTable) {
    this.priceDao = priceDao;
    this.brokerFactory = brokerFactory;
    this.barDataFactory = barDataFactory;
    this.boardLotTable = boardLotTable;
    this.varCalculatorFactory = varCalculatorFactory;
  }

  public Simulation create(final SimulationParams params) {
    checkNotNull(params, "SimulationParams must not be null");

    final FindRequest findRequest = FindRequest.findSymbol(params.getSymbol())
        .from(params.getFromDate())
        .to(params.getToDate());

    // Find biggest period in the algorithm
    int biggestPeriod = 0;
    for (Variable variable : params.getVariables()) {
      if (biggestPeriod < variable.getPeriod()) {
        biggestPeriod = variable.getPeriod();
      }
    }

    final List<Price> priceList = priceDao.find(findRequest);

    // TODO use factory
    final Account account = new Account(new Portfolio(), new Bookkeeper(), new TradeStatistic(), params.getStartingCash());
    final Broker broker = brokerFactory.createBroker();
    final History history = History.withMaxSize(52);
    final World world = World.builder().account(account).broker(broker).boardLotTable(boardLotTable).history(history)
        .pricesForSymbol(params.getSymbol(), priceList).build();
    final Simulation simulation = new Simulation(world, params, barDataFactory);

    simulation.addVarCalculators(varCalculatorFactory.createForVariables(params.getVariables()));

    final List<Price> preDatePrices = priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol(params.getSymbol())
        .beforeDate(params.getFromDate())
        .numOfValues(biggestPeriod)
        .timeFrame(TimeFrame.ONE_DAY)
        .build());

    simulation.preCalc(preDatePrices);

    return simulation;
  }
}
