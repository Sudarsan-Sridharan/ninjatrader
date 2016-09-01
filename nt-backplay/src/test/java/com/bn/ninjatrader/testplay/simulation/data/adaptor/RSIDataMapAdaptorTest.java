package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataType;

/**
 * Created by Brad on 8/18/16.
 */
public class RSIDataMapAdaptorTest extends AbstractPeriodDataMapAdaptorTest {

  @Override
  public DataType provideValidDataType() {
    return DataType.RSI_10;
  }

  @Override
  public DataMapAdaptor<Value> provideDataMapAdaptorForPeriod(int period) {
    return RSIDataMapAdaptor.forPeriod(period);
  }
}
