package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.calculator.util.CalculatingStack;
import com.bn.ninjatrader.calculator.util.RSICalculatingStack;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Relative Strength Index Calculator.
 *
 * Created by Brad on 5/28/16.
 */
@Singleton
public class RSICalculator extends AbstractValueCalculator<RSIValue> {

  private static final Logger log = LoggerFactory.getLogger(RSICalculator.class);

  @Override
  public List<Value> calcForPeriod(final CalcParams<RSIValue> params, final int period) {
    final Optional<RSIValue> priorValue = params.getPriorValueForPeriod(period);

    final RSICalculatingStack stack;
    if (priorValue.isPresent()) {
      stack = RSICalculatingStack.withSizeAndContinueFrom(period, priorValue.get());
    } else {
      stack = RSICalculatingStack.withSize(period);
    }
    return calcWithStack(params.getPrices(), stack);
  }

  @Override
  public RSIValue createValue(final Price price, final CalculatingStack<Price> stack) {
    final RSICalculatingStack rsiCalculatingStack = (RSICalculatingStack) stack;
    final RSIValue rsiValue = new RSIValue(price.getDate(),
        stack.getValue(),
        rsiCalculatingStack.getAvgGain(),
        rsiCalculatingStack.getAvgLoss());
    return rsiValue;
  }
}
