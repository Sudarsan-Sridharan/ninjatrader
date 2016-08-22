package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.SimpleAverageDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
@Singleton
public class SimpleAverageDataFinder implements DataFinder<Value> {

  private static final List<DataType> SUPPORTED_DATA_TYPES;

  static {
    List<DataType> supportedDataTypes = Lists.newArrayList();
    for (DataType dataType : DataType.values()) {
      if (dataType.name().startsWith("SMA_")) {
        supportedDataTypes.add(dataType);
      }
    }
    SUPPORTED_DATA_TYPES = Collections.unmodifiableList(supportedDataTypes);
  }


  @Inject
  private SimpleAverageDao simpleAverageDao;

  @Override
  public List<Value> find(FindRequest findRequest) {
    return simpleAverageDao.find(findRequest);
  }

  @Override
  public List<DataType> getSupportedDataTypes() {
    return SUPPORTED_DATA_TYPES;
  }
}
