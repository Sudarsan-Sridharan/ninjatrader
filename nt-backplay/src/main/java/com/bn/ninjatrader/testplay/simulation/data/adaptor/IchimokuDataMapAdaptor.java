package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.common.data.DataType;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/17/16.
 */
@Singleton
public class IchimokuDataMapAdaptor implements DataMapAdaptor<Ichimoku> {

  @Override
  public DataMap toDataMap(Ichimoku ichimoku) {
    Preconditions.checkNotNull(ichimoku);

    DataMap dataMap = new DataMap();
    dataMap.put(DataType.CHIKOU, ichimoku.getChikou());
    dataMap.put(DataType.TENKAN, ichimoku.getTenkan());
    dataMap.put(DataType.KIJUN, ichimoku.getKijun());
    dataMap.put(DataType.SENKOU_A, ichimoku.getSenkouA());
    dataMap.put(DataType.SENKOU_B, ichimoku.getSenkouB());
    return dataMap;
  }
}
