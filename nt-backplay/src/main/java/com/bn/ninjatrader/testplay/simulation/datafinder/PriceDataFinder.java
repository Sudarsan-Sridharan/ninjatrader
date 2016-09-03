package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.testplay.simulation.SimulationParams;
import com.bn.ninjatrader.testplay.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.forSymbol;
import static com.bn.ninjatrader.testplay.simulation.data.DataType.*;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class PriceDataFinder implements DataFinder<Price> {

  private static final List<DataType> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(PRICE_OPEN, PRICE_HIGH, PRICE_LOW, PRICE_CLOSE, VOLUME));

  @Inject
  private PriceDao priceDao;

  @Inject
  private PriceDataMapAdaptor dataMapAdaptor;

  @Override
  public List<SimulationData<Price>> find(SimulationParams params, int requiredDataSize) {
    List<Price> priceList = priceDao.find(forSymbol(params.getSymbol())
        .from(params.getFromDate())
        .to(params.getToDate()));
    return Lists.newArrayList(new SimulationData(priceList, dataMapAdaptor));
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
