package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.calculator.util.SMACalculatingStack;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Simple Moving Average Calculator.
 *
 * Created by Brad on 5/28/16.
 */
@Singleton
public class SMACalculator extends AbstractValueCalculator {

  private static final Logger LOG = LoggerFactory.getLogger(SMACalculator.class);

  @Override
  public List<Value> calcForPeriod(CalcParams params, int period) {
    return calcWithStack(params.getPrices(), SMACalculatingStack.withFixedSize(period));
  }
}
