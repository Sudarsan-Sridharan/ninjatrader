package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.simulation.data.DataType;

/**
 * Created by Brad on 8/17/16.
 */
public class EMADataMapAdaptor extends AbstractPeriodDataMapAdaptor {

  public static final EMADataMapAdaptor forPeriod(int period) {
    return new EMADataMapAdaptor(period);
  }

  public EMADataMapAdaptor(int period) {
    super(period);
  }

  @Override
  public DataType getDataType() {
    return DataType.EMA;
  }
}
