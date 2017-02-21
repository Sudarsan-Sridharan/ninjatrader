package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 4/30/16.
 */
public interface PriceDao<S, F> {

  void save(final S req);

  List<Price> find(final F findRequest);

  Set<String> findAllSymbols();

  List<Price> findBeforeDate(FindBeforeDateRequest build);
}
