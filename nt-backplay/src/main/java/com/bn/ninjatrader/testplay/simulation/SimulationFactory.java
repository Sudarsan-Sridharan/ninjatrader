package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.bn.ninjatrader.testplay.simulation.guice.annotation.AllDataFinders;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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
  private Provider<Simulation> simulationProvider;

  @Inject
  private PriceDao priceDao;

  public Simulation create(TestPlayParameters params) {
    Set<DataType> dataTypes = params.getDataTypes();

    String symbol = params.getSymbol();
    LocalDate fromDate = params.getFromDate();
    LocalDate toDate = params.getToDate();

    List<Price> priceList = priceDao.find(forSymbol(symbol).from(fromDate).to(toDate));

    Simulation simulation = simulationProvider.get();
    return simulation;
  }
}
