package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataType;

/**
 * Created by Brad on 8/18/16.
 */
public class SMADataMapAdaptorTest extends AbstractPeriodDataMapAdaptorTest {

  @Override
  public DataType provideValidDataType() {
    return DataType.SMA_10;
  }

  @Override
  public DataMapAdaptor<Value> provideDataMapAdaptorForPeriod(int period) {
    return SMADataMapAdaptor.forPeriod(period);
  }
}
