package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.bn.ninjatrader.testplay.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import com.bn.ninjatrader.testplay.simulation.datafinder.DataFinder;
import com.bn.ninjatrader.testplay.simulation.guice.annotation.AllDataFinders;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.bn.ninjatrader.model.dao.period.FindRequest.forSymbol;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class SimulationFactory {

  private static final Logger log = LoggerFactory.getLogger(SimulationFactory.class);

  @Inject
  @AllDataFinders
  private List<DataFinder> dataFinders;

  @Inject
  private PriceDao priceDao;

  @Inject
  private BrokerFactory brokerFactory;

  public Simulation create(SimulationParameters params) {
    FindRequest findRequest = forSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    List<Price> priceList = priceDao.find(findRequest);

    Account account = Account.withStartingCash(params.getStartingCash());
    Broker broker = brokerFactory.createBroker(account);
    Simulation simulation = new Simulation(account, broker, params, priceList);
    addSimulationData(simulation, params, priceList.size());
    return simulation;
  }

  private void addSimulationData(Simulation simulation, SimulationParameters params, int requiredDataSize) {
    Set<DataType> dataTypes = params.getDataTypes();
    for (DataFinder dataFinder : dataFinders) {
      dataFinder.getSupportedDataTypes();
      if (!Collections.disjoint(dataFinder.getSupportedDataTypes(), dataTypes)) {
        List<SimulationData> dataList = dataFinder.find(params, requiredDataSize);
        simulation.addSimulationData(dataList);
      }
    }
  }
}
