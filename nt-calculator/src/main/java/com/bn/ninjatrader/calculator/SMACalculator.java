package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.util.SMACalculatingStack;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Simpe Moving Average Calculator.
 *
 * Created by Brad on 5/28/16.
 */
@Singleton
public class SMACalculator extends AbstractCalculatorForPeriod {

  private static final Logger log = LoggerFactory.getLogger(SMACalculator.class);

  @Override
  public List<Value> calc(List<Price> priceList, int period) {
    return calcWithStack(priceList, SMACalculatingStack.withFixedSize(period));
  }
}
