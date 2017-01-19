package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.RSIDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.adaptor.RSIDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Set;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * Relative Strength Index DataFinder
 *
 * Created by Brad on 8/20/16.
 */
@Singleton
public class RSIDataProvider implements DataProvider<Value> {

  private static final List<DataType> SUPPORTED_DATA_TYPES = Lists.newArrayList(DataType.RSI);

  @Inject
  private RSIDao rsiDao;

  @Override
  public List<SimulationData<Value>> find(SimulationParams params, int requiredDataSize) {
    List<SimulationData<Value>> simulationDataList = Lists.newArrayList();
    FindRequest findRequest = findSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    Set<Variable> variables = params.getVariables();

    for (Variable variable : variables) {
      if (variable.getDataType() == DataType.RSI) {
        int period = variable.getPeriod();
        findRequest.period(period);
        List<RSIValue> valueList = rsiDao.find(findRequest);
        ListUtil.fillToSize(valueList, RSIValue.empty(), requiredDataSize);
        simulationDataList.add(new SimulationData(valueList, RSIDataMapAdaptor.forPeriod(period)));
      }
    }
    return simulationDataList;
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }

}
