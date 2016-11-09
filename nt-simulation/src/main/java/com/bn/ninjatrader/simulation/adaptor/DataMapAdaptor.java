package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.simulation.data.DataMap;

/**
 * Created by Brad on 8/17/16.
 */
public interface DataMapAdaptor<T> {

  DataMap toDataMap(T t);
}
