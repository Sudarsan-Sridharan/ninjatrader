package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.model.deprecated.Value;

import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 7/28/16.
 */
public interface ValueCalculator {

  Map<Integer,List<Value>> calc(CalcParams params);
}
