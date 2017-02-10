package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.model.*;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.data.provider.DataProvider;
import com.bn.ninjatrader.simulation.annotation.AllDataProviders;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class SimulationFactory {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactory.class);

  private final List<DataProvider> dataFinders;
  private final PriceDao priceDao;
  private final BrokerFactory brokerFactory;
  private final BarDataFactory barDataFactory;
  private final BoardLotTable boardLotTable;

  @Inject
  public SimulationFactory(@AllDataProviders final List<DataProvider> dataFinders,
                           final PriceDao priceDao,
                           final BrokerFactory brokerFactory,
                           final BarDataFactory barDataFactory,
                           final BoardLotTable boardLotTable) {
    this.dataFinders = dataFinders;
    this.priceDao = priceDao;
    this.brokerFactory = brokerFactory;
    this.barDataFactory = barDataFactory;
    this.boardLotTable = boardLotTable;
  }

  public Simulation create(final SimulationParams params) {
    checkNotNull(params, "SimulationParams must not be null");

    final FindRequest findRequest = findSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    final List<Price> priceList = priceDao.find(findRequest);

    // TODO use factory
    final Account account = new Account(new Portfolio(), new Bookkeeper(), new TradeStatistic(), params.getStartingCash());
    final Broker broker = brokerFactory.createBroker();
    final History history = History.withMaxSize(52);
    final World world = World.builder().account(account).broker(broker).boardLotTable(boardLotTable).history(history)
        .pricesForSymbol(params.getSymbol(), priceList).build();
    final Simulation simulation = new Simulation(world, params, barDataFactory);

    addSimulationData(simulation, priceList.size());

    return simulation;
  }

  private void addSimulationData(final Simulation simulation,
                                 final int requiredDataSize) {
    final SimulationParams params = simulation.getSimulationParams();
    final Set<String> dataTypes = params.getDataTypes();
    for (final DataProvider dataFinder : dataFinders) {
      if (!Collections.disjoint(dataFinder.getSupportedDataTypes(), dataTypes)) {
        final List<SimulationData> dataList = dataFinder.find(params, requiredDataSize);
        simulation.addSimulationData(dataList);
      }
    }
  }
}
