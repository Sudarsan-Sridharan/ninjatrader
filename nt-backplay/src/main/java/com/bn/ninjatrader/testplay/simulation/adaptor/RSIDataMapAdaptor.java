package com.bn.ninjatrader.testplay.simulation.adaptor;

/**
 * Created by Brad on 8/17/16.
 */
public class RSIDataMapAdaptor extends AbstractPeriodDataMapAdaptor {

  private static final String DATA_TYPE_PREFIX = "RSI_";

  public static final RSIDataMapAdaptor forPeriod(int period) {
    return new RSIDataMapAdaptor(period);
  }

  public RSIDataMapAdaptor(int period) {
    super(period);
  }

  @Override
  public String getDataTypePrefix() {
    return DATA_TYPE_PREFIX;
  }
}
