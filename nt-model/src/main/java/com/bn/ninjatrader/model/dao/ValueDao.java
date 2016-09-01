package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.bn.ninjatrader.model.dao.period.SaveRequest;
import org.jongo.MongoCollection;

import java.util.List;

/**
 * Created by Brad on 8/16/16.
 */
public interface ValueDao {

  void save(SaveRequest saveRequest);

  List<Value> find(FindRequest findRequest);

  MongoCollection getMongoCollection();
}
