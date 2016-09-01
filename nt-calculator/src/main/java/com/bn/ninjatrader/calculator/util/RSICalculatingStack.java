package com.bn.ninjatrader.calculator.util;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public static RSICalculatingStack withFixedSize(int fixedSize) {
    return new RSICalculatingStack(fixedSize);
  }

  private RSICalculatingStack(int fixedSize) {
    super(fixedSize);
    this.fixedSize = fixedSize;
  }

  @Override
  protected void handleAfterAdd(Price addedPrice) {
    if (isSmooth) {
      calcSmoothAverages(addedPrice);
    } else {
      calcAverages(addedPrice);
    }

    if (!isSmooth && size() == fixedSize) {
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
    return NumUtil.round(relativeStrengthIndex, DECIMAL_PLACES);
  }

  @Override
  public double getValue() {
    if (size() != getFixedSize()) {
      return Double.NaN;
    }
    return calcRelativeStrengthIndex();
  }
}
