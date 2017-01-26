package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.SMADao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.adaptor.SMADataMapAdaptor;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.core.SimulationData;
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
public class SMADataProvider implements DataProvider<Value> {

  private static final Logger LOG = LoggerFactory.getLogger(SMADataProvider.class);

  private static final List<String> SUPPORTED_DATA_TYPES = Lists.newArrayList(DataType.SMA);

  @Inject
  private SMADao smaDao;

  @Override
  public List<SimulationData<Value>> find(final SimulationParams params, final int requiredDataSize) {
    final List<SimulationData<Value>> simulationDataList = Lists.newArrayList();
    final FindRequest findRequest = findSymbol(params.getSymbol()).from(params.getFromDate()).to(params.getToDate());
    final Set<Variable> variables = params.getVariables();

    for (final Variable variable : variables) {
      if (variable.getDataType() == DataType.SMA) {
        int period = variable.getPeriod();
        findRequest.period(period);
        final List<Value> valueList = smaDao.find(findRequest);
        ListUtil.fillToSize(valueList, Value.empty(), requiredDataSize);
        simulationDataList.add(new SimulationData(valueList, SMADataMapAdaptor.forPeriod(period)));
      }
    }
    return simulationDataList;
  }

  @Override
  public List<String> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }

}
