package com.bn.ninjatrader.simulation.datafinder;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.IchimokuDao;
import com.bn.ninjatrader.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class IchimokuDataFinder implements DataFinder<Ichimoku> {

  private static final List<DataType> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(DataType.CHIKOU, DataType.TENKAN, DataType.KIJUN, DataType.SENKOU_A, DataType.SENKOU_B));

  @Inject
  private IchimokuDao ichimokuService;

  @Inject
  private IchimokuDataMapAdaptor dataMapAdaptor;

  @Override
  public List<SimulationData<Ichimoku>> find(SimulationParams params, int requiredDataSize) {
    List<Ichimoku> ichimokuList = ichimokuService.find(findSymbol(params.getSymbol())
        .from(params.getFromDate())
        .to(params.getToDate()));
    ListUtil.fillToSize(ichimokuList, Ichimoku.empty(), requiredDataSize);
    return Lists.newArrayList(new SimulationData(ichimokuList, dataMapAdaptor));
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
