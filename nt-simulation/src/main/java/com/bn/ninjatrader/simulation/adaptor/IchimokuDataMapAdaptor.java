package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import static com.bn.ninjatrader.simulation.operation.Variables.*;

/**
 * Created by Brad on 8/17/16.
 */
@Singleton
public class IchimokuDataMapAdaptor implements DataMapAdaptor<Ichimoku> {

  @Override
  public DataMap toDataMap(Ichimoku ichimoku) {
    Preconditions.checkNotNull(ichimoku);

    DataMap dataMap = new DataMap();
    dataMap.put(ICHIMOKU_CHIKOU, ichimoku.getChikou());
    dataMap.put(ICHIMOKU_TENKAN, ichimoku.getTenkan());
    dataMap.put(ICHIMOKU_KIJUN, ichimoku.getKijun());
    dataMap.put(ICHIMOKU_SENKOU_A, ichimoku.getSenkouA());
    dataMap.put(ICHIMOKU_SENKOU_B, ichimoku.getSenkouB());
    return dataMap;
  }
}
