package com.bn.ninjatrader.simulation.datafinder;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.SMADao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.adaptor.SMADataMapAdaptor;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * Simple Moving Average DataFinder
 *
 * Created by Brad on 8/20/16.
 */
@Singleton
public class SMADataFinder implements DataFinder<Value> {

  private static final Logger LOG = LoggerFactory.getLogger(SMADataFinder.class);

  private static final List<DataType> SUPPORTED_DATA_TYPES = Lists.newArrayList(DataType.SMA);

  @Inject
  private SMADao simpleAverageDao;

  @Override
  public List<SimulationData<Value>> find(SimulationParams params, int requiredDataSize) {
    List<SimulationData<Value>> simulationDataList = Lists.newArrayList();
    FindRequest findRequest = findSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    Set<Variable> variables = params.getVariables();

    for (Variable variable : variables) {
      if (variable.getDataType() == DataType.SMA) {
        int period = variable.getPeriod();
        findRequest.period(period);
        List<Value> valueList = simpleAverageDao.find(findRequest);
        ListUtil.fillToSize(valueList, Value.empty(), requiredDataSize);
        simulationDataList.add(new SimulationData(valueList, SMADataMapAdaptor.forPeriod(period)));
      }
    }
    return simulationDataList;
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }

}
