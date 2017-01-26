package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.simulation.adaptor.DataMapAdaptor;
import com.bn.ninjatrader.simulation.data.DataMap;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Holds data per bar.
 * Created by Brad on 8/17/16.
 */
public class SimulationData<T> {

  private final List<T> dataList;
  private final DataMapAdaptor<T> dataAdaptor;

  public SimulationData(final List<T> dataList,
                        final DataMapAdaptor<T> dataAdaptor) {
    checkNotNull(dataList, "dataList must not be null.");
    checkArgument(!dataList.isEmpty(), "dataList must not be empty.");
    checkNotNull(dataAdaptor, "dataAdaptor must not be null.");

    this.dataList = dataList;
    this.dataAdaptor = dataAdaptor;
  }

  public DataMap getDataAtIndex(int index) {
    final T t = dataList.get(index);
    return dataAdaptor.toDataMap(t);
  }

  public int size() {
    return dataList.size();
  }
}
