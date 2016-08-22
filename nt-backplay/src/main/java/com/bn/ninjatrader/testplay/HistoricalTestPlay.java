package com.bn.ninjatrader.testplay;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.SimpleAverageDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.bn.ninjatrader.testplay.condition.Conditions;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.bn.ninjatrader.testplay.simulation.Simulation;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import com.bn.ninjatrader.testplay.simulation.data.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.data.adaptor.SimpleAverageDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.testplay.simulation.guice.annotation.AllDataFinders;
import com.google.inject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.common.data.DataType.*;
import static com.bn.ninjatrader.model.dao.period.FindRequest.forSymbol;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class HistoricalTestPlay implements TestPlay {
  private static final Logger log = LoggerFactory.getLogger(HistoricalTestPlay.class);

  @Inject
  private PriceDao priceDao;

  @Inject
  private SimpleAverageDao simpleAverageDao;

  @Inject
  private IchimokuService ichimokuService;

  @Inject
  @AllDataFinders
  private List<DataFinder> dataFinders;

  @Inject
  private Provider<Simulation> simulationProvider;

  @Override
  public void test(TestPlayParameters params) {

    String symbol = params.getSymbol();
    LocalDate fromDate = params.getFromDate();
    LocalDate toDate = params.getToDate();

    List<Price> priceList = priceDao.find(forSymbol(symbol).from(fromDate).to(toDate));
    List<Ichimoku> ichimokuList = ichimokuService.find(forSymbol(symbol).from(fromDate).to(toDate));
    List<Value> sma21List = simpleAverageDao.find(forSymbol(symbol).from(fromDate).to(toDate).period(21));

    ListUtil.fillToSize(ichimokuList, Ichimoku.empty(), priceList.size());
    ListUtil.fillToSize(sma21List, Value.empty(), priceList.size());

    SimulationData<Ichimoku> ichimokuData = new SimulationData<>(ichimokuList, new IchimokuDataMapAdaptor());
    SimulationData<Value> sma21Data = new SimulationData<>(sma21List, SimpleAverageDataMapAdaptor.forPeriod(21));

    Simulation simulation = simulationProvider.get();
    simulation.setup(params, priceList);
    simulation.addSimulationData(ichimokuData);
    simulation.addSimulationData(sma21Data);
    simulation.play();
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtCalculatorModule(),
        new NtSimulationModule()
    );
    HistoricalTestPlay historicalTestPlay = injector.getInstance(HistoricalTestPlay.class);

    TestPlayParameters params = new TestPlayParameters();
    params.setSymbol("DD");
    params.setFromDate(LocalDate.now().minusYears(10));
    params.setToDate(LocalDate.now());
    params.setStartingEquity(100000);

    params.setBuyCondition(Conditions.create()
        .add(Conditions.gt(PRICE_CLOSE, SMA_21))
        .add(Conditions.gt(PRICE_CLOSE, TENKAN))
        .add(Conditions.gt(TENKAN, KIJUN))
        .add(Conditions.gte(TENKAN, 0d))
        .add(Conditions.gt(KIJUN, 0d)));

    params.setSellCondition(Conditions.create()
        .add(Conditions.lt(PRICE_CLOSE, SMA_21)));

    historicalTestPlay.test(params);
  }
}
