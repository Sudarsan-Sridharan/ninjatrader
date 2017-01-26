package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.util.ListUtil;
import com.bn.ninjatrader.model.dao.IchimokuDao;
import com.bn.ninjatrader.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
import static com.bn.ninjatrader.simulation.model.DataType.*;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class IchimokuDataProvider implements DataProvider<Ichimoku> {

  private static final List<String> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(CHIKOU, TENKAN, KIJUN, SENKOU_A, SENKOU_B));

  private final IchimokuDao ichimokuDao;
  private final IchimokuDataMapAdaptor dataMapAdaptor;

  @Inject
  public IchimokuDataProvider(final IchimokuDao ichimokuDao,
                              final IchimokuDataMapAdaptor dataMapAdaptor) {
    this.ichimokuDao = ichimokuDao;
    this.dataMapAdaptor = dataMapAdaptor;
  }

  @Override
  public List<SimulationData<Ichimoku>> find(final SimulationParams params, final int requiredDataSize) {
    final List<Ichimoku> ichimokuList = ichimokuDao.find(findSymbol(params.getSymbol())
        .from(params.getFromDate())
        .to(params.getToDate()));
    ListUtil.fillToSize(ichimokuList, Ichimoku.empty(), requiredDataSize);
    return Lists.newArrayList(new SimulationData(ichimokuList, dataMapAdaptor));
  }

  @Override
  public List<String> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
