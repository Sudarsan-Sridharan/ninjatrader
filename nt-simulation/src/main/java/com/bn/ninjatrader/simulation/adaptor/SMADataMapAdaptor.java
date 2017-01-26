package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.simulation.model.DataType;

/**
 * Created by Brad on 8/17/16.
 */
public class SMADataMapAdaptor extends AbstractPeriodDataMapAdaptor {

  public static final SMADataMapAdaptor forPeriod(int period) {
    return new SMADataMapAdaptor(period);
  }

  public SMADataMapAdaptor(int period) {
    super(period);
  }

  @Override
  public String getDataType() {
    return DataType.SMA;
  }
}
