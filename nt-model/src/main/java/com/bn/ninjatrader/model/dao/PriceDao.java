package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 4/30/16.
 */
public interface PriceDao {

  void save(final SavePriceRequest req);

  List<Price> find(final FindPriceRequest findRequest);

  Set<String> findAllSymbols();

  List<Price> findBeforeDate(final FindBeforeDateRequest build);
}
