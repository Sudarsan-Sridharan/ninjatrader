package com.bn.ninjatrader.model.appengine;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.googlecode.objectify.annotation.Entity;

/**
 * @author bradwee2000@gmail.com
 */
@Entity
public class EmaDocument extends AbstractPeriodDocument<Value> {

  public EmaDocument(final String symbol, final int year, final TimeFrame timeFrame, final int period) {
    super(symbol, year, timeFrame, period);
  }
}
