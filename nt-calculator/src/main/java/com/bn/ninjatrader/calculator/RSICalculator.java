package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.util.CalculatingStack;
import com.bn.ninjatrader.calculator.util.RSICalculatingStack;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Relative Strength Index Calculator.
 *
 * Created by Brad on 5/28/16.
 */
@Singleton
public class RSICalculator extends AbstractValueCalculator {

  private static final Logger log = LoggerFactory.getLogger(RSICalculator.class);

  @Override
  public List<Value> calc(List<Price> priceList, int period) {
    return calcWithStack(priceList, RSICalculatingStack.withSize(period));
  }

  @Override
  public RSIValue createValue(Price price, CalculatingStack<Price> stack) {
    RSICalculatingStack rsiCalculatingStack = (RSICalculatingStack) stack;
    RSIValue rsiValue = new RSIValue(price.getDate(),
        stack.getValue(),
        rsiCalculatingStack.getAvgGain(),
        rsiCalculatingStack.getAvgLoss());
    return rsiValue;
  }
}
