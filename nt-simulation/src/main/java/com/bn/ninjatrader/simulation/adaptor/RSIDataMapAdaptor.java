package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.simulation.data.DataType;

/**
 * Created by Brad on 8/17/16.
 */
public class RSIDataMapAdaptor extends AbstractPeriodDataMapAdaptor {

  public static final RSIDataMapAdaptor forPeriod(int period) {
    return new RSIDataMapAdaptor(period);
  }

  public RSIDataMapAdaptor(int period) {
    super(period);
  }

  @Override
  public DataType getDataType() {
    return DataType.RSI;
  }
}