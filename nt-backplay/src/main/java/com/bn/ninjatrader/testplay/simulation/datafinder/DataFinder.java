package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.testplay.simulation.SimulationParameters;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;

import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
public interface DataFinder<T> {

  List<SimulationData<T>> find(SimulationParameters params, int requiredDataSize);

  List<DataType> getSupportedDataTypes();
}
