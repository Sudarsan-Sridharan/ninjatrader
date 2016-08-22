package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.model.dao.period.FindRequest;

import java.util.List;

/**
 * Created by Brad on 8/20/16.
 */
public interface DataFinder<T> {

  List<T> find(FindRequest findRequest);

  List<DataType> getSupportedDataTypes();
}
