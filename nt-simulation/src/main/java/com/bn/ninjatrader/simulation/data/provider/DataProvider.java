package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.core.SimulationData;

import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
public interface DataProvider<T> {

  List<SimulationData<T>> find(final SimulationParams params, final int requiredDataSize);

  List<String> getSupportedDataTypes();
}
