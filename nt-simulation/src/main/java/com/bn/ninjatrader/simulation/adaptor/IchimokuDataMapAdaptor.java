package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
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
    dataMap.put(Variable.of(DataType.CHIKOU), ichimoku.getChikou());
    dataMap.put(Variable.of(DataType.TENKAN), ichimoku.getTenkan());
    dataMap.put(Variable.of(DataType.KIJUN), ichimoku.getKijun());
    dataMap.put(Variable.of(DataType.SENKOU_A), ichimoku.getSenkouA());
    dataMap.put(Variable.of(DataType.SENKOU_B), ichimoku.getSenkouB());
    return dataMap;
  }
}
