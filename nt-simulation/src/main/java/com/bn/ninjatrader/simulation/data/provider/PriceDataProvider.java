package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
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
public class PriceDataProvider implements DataProvider<Price> {

  private static final List<String> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(PRICE_OPEN, PRICE_HIGH, PRICE_LOW, PRICE_CLOSE, VOLUME));

  private final PriceDao priceDao;
  private final PriceDataMapAdaptor dataMapAdaptor;

  @Inject
  public PriceDataProvider(final PriceDao priceDao,
                           final PriceDataMapAdaptor dataMapAdaptor) {
    this.priceDao = priceDao;
    this.dataMapAdaptor = dataMapAdaptor;
  }

  @Override
  public List<SimulationData<Price>> find(final SimulationParams params, final int requiredDataSize) {
    final List<Price> priceList = priceDao.find(findSymbol(params.getSymbol())
        .from(params.getFromDate())
        .to(params.getToDate()));
    return Lists.newArrayList(new SimulationData(priceList, dataMapAdaptor));
  }

  @Override
  public List<String> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
