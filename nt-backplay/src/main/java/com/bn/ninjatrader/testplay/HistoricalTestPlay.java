package com.bn.ninjatrader.testplay;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.bn.ninjatrader.testplay.condition.Conditions;
import com.bn.ninjatrader.testplay.simulation.Simulation;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.bn.ninjatrader.testplay.type.DataType;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class HistoricalTestPlay implements TestPlay {
  private static final Logger log = LoggerFactory.getLogger(HistoricalTestPlay.class);

  @Inject
  private PriceDao priceDao;

  @Inject
  private IchimokuService ichimokuService;

  @Override
  public void test(TestPlayParameters params) {
    String symbol = params.getSymbol();
    LocalDate fromDate = params.getFromDate();
    LocalDate toDate = params.getToDate();

    List<Price> priceList = priceDao.findByDateRange(symbol, fromDate, toDate);
    List<Ichimoku> ichimokuList = ichimokuService.getIchimoku(symbol, fromDate, toDate);

    Simulation simulation = new Simulation(params, priceList);
    simulation.setIchimokuList(ichimokuList);
    simulation.play();
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtCalculatorModule()
    );
    HistoricalTestPlay historicalTestPlay = injector.getInstance(HistoricalTestPlay.class);

    TestPlayParameters params = new TestPlayParameters();
    params.setSymbol("MEG");
    params.setFromDate(LocalDate.now().minusYears(5));
    params.setToDate(LocalDate.now().minusYears(0));
    params.setStartingEquity(100000);

    params.setBuyCondition(Conditions.create()
        .add(Conditions.gt(DataType.PRICE_CLOSE, DataType.TENKAN))
        .add(Conditions.gt(DataType.PRICE_CLOSE, DataType.KIJUN))
        .add(Conditions.gt(DataType.TENKAN, DataType.KIJUN))
        .add(Conditions.gte(DataType.TENKAN, 0d))
        .add(Conditions.gt(DataType.KIJUN, 0d)));

    params.setSellCondition(Conditions.create()
        .add(Conditions.lte(DataType.PRICE_CLOSE, DataType.KIJUN)));

    historicalTestPlay.test(params);
  }
}
