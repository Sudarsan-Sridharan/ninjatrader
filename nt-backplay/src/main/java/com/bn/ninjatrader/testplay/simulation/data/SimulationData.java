package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.testplay.simulation.adaptor.DataMapAdaptor;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by Brad on 8/17/16.
 */
public class SimulationData<T> {

  private List<T> dataList;
  private DataMapAdaptor<T> dataAdaptor;

  public SimulationData(List<T> dataList, DataMapAdaptor<T> dataAdaptor) {
    Preconditions.checkNotNull(dataList);
    Preconditions.checkArgument(!dataList.isEmpty());
    Preconditions.checkNotNull(dataAdaptor);

    this.dataList = dataList;
    this.dataAdaptor = dataAdaptor;
  }

  public DataMap getDataMap(int index) {
    return dataAdaptor.toDataMap(dataList.get(index));
  }

  public int size() {
    return dataList.size();
  }
}
