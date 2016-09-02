package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.simulation.SimulationParams;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;

import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
public interface DataFinder<T> {

  List<SimulationData<T>> find(SimulationParams params, int requiredDataSize);

  List<DataType> getSupportedDataTypes();
}
