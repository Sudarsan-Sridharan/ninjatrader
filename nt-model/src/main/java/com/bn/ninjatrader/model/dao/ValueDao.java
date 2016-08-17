package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.dao.period.SaveRequest;

/**
 * Created by Brad on 8/16/16.
 */
public interface ValueDao {

  void save(SaveRequest saveRequest);
}
