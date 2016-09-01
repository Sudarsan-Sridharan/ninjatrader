package com.bn.ninjatrader.testplay.simulation.adaptor;

/**
 * Created by Brad on 8/17/16.
 */
public class SMADataMapAdaptor extends AbstractPeriodDataMapAdaptor {

  private static final String DATA_TYPE_PREFIX = "SMA_";

  public static final SMADataMapAdaptor forPeriod(int period) {
    return new SMADataMapAdaptor(period);
  }

  public SMADataMapAdaptor(int period) {
    super(period);
  }

  @Override
  public String getDataTypePrefix() {
    return DATA_TYPE_PREFIX;
  }
}
