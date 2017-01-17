package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stack that calculates for Simple Moving Average on each insert.
 *
 * Created by Brad on 7/11/16.
 */
public class EMACalculatingStack extends FixedStack<Price> implements CalculatingStack<Price> {
  private static final Logger LOG = LoggerFactory.getLogger(EMACalculatingStack.class);

  public static EMACalculatingStack withFixedSize(final int fixedSize) {
    return new EMACalculatingStack(fixedSize);
  }
  public static EMACalculatingStack withFixedSizeAndPriorValue(final int fixedSize,
                                                               final Value value) {
    return new EMACalculatingStack(fixedSize, value.getValue());
  }
  public static EMACalculatingStack withFixedSizeAndPriorValue(final int fixedSize, final double value) {
    return new EMACalculatingStack(fixedSize, value);
  }

  private final double multiplier;
  private final int fixedSize;

  private double emaValue = Double.NaN;

  private EMACalculatingStack(final int fixedSize) {
    super(fixedSize);
    this.fixedSize = fixedSize;
    this.multiplier = 2.0 / (fixedSize + 1);
  }

  /**
   * Continue with smooth averages using previous average values provided.
   * @param fixedSize
   * @param priorValue - The previous average values to continue calculating from.
   */
  private EMACalculatingStack(final int fixedSize, final double priorValue) {
    this(fixedSize);
    this.emaValue = priorValue;
  }

  @Override
  protected void handleAfterAdd(final Price addedPrice) {
    // If starting EMA is not set, need to calculate initial value using simple average
    if (Double.isNaN(emaValue)) {
      // Wait til stack is full
      if (this.size() == fixedSize) {
        // Set initial value using simple average
        final SMACalculatingStack sma = SMACalculatingStack.withFixedSize(fixedSize);
        sma.addAll(getList());
        emaValue = sma.getValue();
      }
    } else {
      emaValue = (addedPrice.getClose() - emaValue) * multiplier + emaValue;
    }
  }

  @Override
  public double getValue() {
    return emaValue;
  }
}
