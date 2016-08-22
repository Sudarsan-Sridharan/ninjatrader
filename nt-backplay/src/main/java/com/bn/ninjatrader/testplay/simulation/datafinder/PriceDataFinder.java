package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.common.data.DataType.*;
import static com.bn.ninjatrader.common.data.DataType.VOLUME;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class PriceDataFinder implements DataFinder<Price> {

  private static final List<DataType> SUPPORTED_DATA_TYPES = Collections.unmodifiableList(
      Lists.newArrayList(PRICE_OPEN, PRICE_HIGH, PRICE_LOW, PRICE_CLOSE, VOLUME));

  @Inject
  private PriceDao priceDao;

  @Override
  public List<Price> find(FindRequest findRequest) {
    return priceDao.find(findRequest);
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
