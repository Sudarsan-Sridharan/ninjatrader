package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import org.jongo.MongoCollection;

import java.util.List;

/**
 * Created by Brad on 8/16/16.
 */
public interface ValueDao<T extends Value> {

  void save(SaveRequest saveRequest);

  List<T> find(FindRequest findRequest);

  MongoCollection getMongoCollection();
}
