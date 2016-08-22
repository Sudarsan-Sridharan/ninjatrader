package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.common.data.DataType.*;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class IchimokuDataFinder implements DataFinder<Ichimoku> {

  private static final List<DataType> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(CHIKOU, TENKAN, KIJUN, SENKOU_A, SENKOU_B));

  @Inject
  private IchimokuService ichimokuService;

  @Override
  public List<Ichimoku> find(FindRequest findRequest) {
    return ichimokuService.find(findRequest);
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
