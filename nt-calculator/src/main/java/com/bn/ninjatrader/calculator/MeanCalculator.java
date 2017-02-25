package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.calculator.util.MeanCalculatingStack;
import com.bn.ninjatrader.model.deprecated.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@Singleton
public class MeanCalculator extends AbstractValueCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(MeanCalculator.class);

  @Override
  public List<Value> calcForPeriod(CalcParams params, int period) {
    return calcWithStack(params.getPrices(), MeanCalculatingStack.withFixedSize(period));
  }
}
