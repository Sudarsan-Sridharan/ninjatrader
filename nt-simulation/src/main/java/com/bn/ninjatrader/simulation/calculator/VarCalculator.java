package com.bn.ninjatrader.simulation.calculator;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.DataMap;

/**
 * @author bradwee2000@gmail.com
 */
public interface VarCalculator {

  DataMap calc(final Price price);
}
