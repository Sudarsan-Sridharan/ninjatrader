package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.deprecated.RSIValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bn.ninjatrader.common.util.NumUtil.round;

/**
 * Stack that calculates for Relative Strength Index on each insert
 * @link http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:relative_strength_index_rsi
 *
 * Created by Brad on 7/11/16.
 */
public class RSICalculatingStack extends FixedStack<Price> implements CalculatingStack<Price> {

  private static final Logger log = LoggerFactory.getLogger(RSICalculatingStack.class);
  private static final int DECIMAL_PLACES = 2;

  private final int fixedSize;

  private double avgGain;
  private double avgLoss;

  private boolean isSmooth = false;

  public static RSICalculatingStack withSize(int fixedSize) {
    return new RSICalculatingStack(fixedSize);
  }

  public static RSICalculatingStack withSizeAndContinueFrom(int fixedSize, RSIValue continueFromValue) {
    return new RSICalculatingStack(fixedSize, continueFromValue);
  }

  private RSICalculatingStack(int fixedSize) {
    super(fixedSize);
    this.fixedSize = fixedSize;
  }

  /**
   * Continue with smooth averages using previous average values provided.
   * @param fixedSize
   * @param continueFromRsiValue - The previous average values to continue calculating from.
   */
  private RSICalculatingStack(int fixedSize, RSIValue continueFromRsiValue) {
    this(fixedSize);
    this.avgGain = continueFromRsiValue.getAvgGain();
    this.avgLoss = continueFromRsiValue.getAvgLoss();
    this.isSmooth = true;
  }

  @Override
  protected void handleAfterAdd(Price addedPrice) {
    if (isSmooth) {
      calcSmoothAverages(addedPrice);
    } else {
      calcAverages(addedPrice);
    }

    // Start smoothing the curve after the first calculation.
    if (!isSmooth && hasValue()) {
      isSmooth = true;
    }
  }

  private void calcAverages(Price price) {
    double priceChange = price.getChange();
    if (priceChange > 0) {
      avgGain += priceChange / fixedSize;
    } else {
      avgLoss += (-priceChange) / fixedSize;
    }
  }

  private void calcSmoothAverages(Price price) {
    double priceChange = price.getChange();
    double gain = priceChange > 0 ? priceChange : 0;
    double loss = priceChange < 0 ? -priceChange : 0;

    avgGain = (avgGain * (fixedSize - 1) + gain) / fixedSize;
    avgLoss = (avgLoss * (fixedSize - 1) + loss) / fixedSize;
  }

  private double calcRelativeStrength() {
    double relativeStrength = avgGain / avgLoss;
    return relativeStrength;
  }

  private double calcRelativeStrengthIndex() {
    double relativeStrength = calcRelativeStrength();
    double relativeStrengthIndex = 100 - (100 / (1 + relativeStrength));
    return round(relativeStrengthIndex, DECIMAL_PLACES);
  }

  @Override
  public double getValue() {
    return hasValue() ? calcRelativeStrengthIndex() : Double.NaN;
  }

  public double getAvgGain() {
    return hasValue() ? avgGain : Double.NaN;
  }

  public double getAvgLoss() {
    return hasValue() ? avgLoss : Double.NaN;
  }

  /**
   * Stack has value if one of the following conditions are met:
   * - stack is full and able to calculate simple average values
   * - previous average values are given, which the stack can continue calculating from.
   * @return
   */
  public boolean hasValue() {
    return size() == fixedSize || isSmooth;
  }
}
