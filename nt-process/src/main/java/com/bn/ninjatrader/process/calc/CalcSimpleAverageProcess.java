package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.SimpleAverageCalculator;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.SimpleAverageDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.bn.ninjatrader.model.dao.period.SaveRequest.forSymbol;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcSimpleAverageProcess extends AbstractCalcForPeriodProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcSimpleAverageProcess.class);
  private static final int[] DEFAULT_PERIODS = {10, 20, 21, 30, 50, 100};

  @Inject
  private SimpleAverageDao simpleAverageDao;

  @Inject
  public CalcSimpleAverageProcess(SimpleAverageCalculator calculator) {
    super(calculator);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }

  @Override
  protected void save(String symbol, int period, List<Value> values) {
    simpleAverageDao.save(forSymbol(symbol).period(period).values(values));
  }
}
