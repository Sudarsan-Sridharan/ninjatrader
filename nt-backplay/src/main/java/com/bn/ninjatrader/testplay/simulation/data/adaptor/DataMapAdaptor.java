package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.testplay.simulation.data.DataMap;

/**
 * Created by Brad on 8/17/16.
 */
public interface DataMapAdaptor<T> {

  DataMap toDataMap(T t);
}
