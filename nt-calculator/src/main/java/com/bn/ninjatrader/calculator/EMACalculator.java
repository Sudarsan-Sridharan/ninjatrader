package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.calculator.util.EMACalculatingStack;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Exponential Moving Average Calculator.
 *
 * Created by Brad on 5/28/16.
 */
@Singleton
public class EMACalculator extends AbstractValueCalculator {

  private static final Logger LOG = LoggerFactory.getLogger(EMACalculator.class);

  @Override
  public List<Value> calcForPeriod(final CalcParams params, final int period) {
    final Optional<Value> priorValue = params.getPriorValueForPeriod(period);
    final EMACalculatingStack stack;

    if (priorValue.isPresent()) {
      stack = EMACalculatingStack.withFixedSizeAndPriorValue(period, priorValue.get());
    } else {
      stack = EMACalculatingStack.withFixedSize(period);
    }
    return calcWithStack(params.getPrices(), stack);
  }
}
