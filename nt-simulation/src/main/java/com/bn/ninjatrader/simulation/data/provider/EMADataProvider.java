package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.EMADao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.adaptor.EMADataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.model.DataType;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.logical.expression.operation.Variable;
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
public class EMADataProvider implements DataProvider<Value> {

  private static final Logger LOG = LoggerFactory.getLogger(EMADataProvider.class);

  private static final List<String> SUPPORTED_DATA_TYPES = Lists.newArrayList(DataType.EMA);

  @Inject
  private EMADao emaDao;

  @Override
  public List<SimulationData<Value>> find(final SimulationParams params, final int requiredDataSize) {
    final List<SimulationData<Value>> simulationDataList = Lists.newArrayList();
    final FindRequest findRequest = findSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    final Set<Variable> variables = params.getVariables();

    for (final Variable variable : variables) {
      if (variable.getDataType() == DataType.EMA) {
        int period = variable.getPeriod();
        findRequest.period(period);
        final List<Value> valueList = emaDao.find(findRequest);
        ListUtil.fillToSize(valueList, Value.empty(), requiredDataSize);
        simulationDataList.add(new SimulationData(valueList, EMADataMapAdaptor.forPeriod(period)));
      }
    }
    return simulationDataList;
  }

  @Override
  public List<String> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }

}
