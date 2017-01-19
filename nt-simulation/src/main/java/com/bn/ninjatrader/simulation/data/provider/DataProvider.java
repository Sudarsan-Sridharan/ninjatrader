package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.core.SimulationParams;

import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
public interface DataProvider<T> {

  List<SimulationData<T>> find(SimulationParams params, int requiredDataSize);

  List<DataType> getSupportedDataTypes();
}
