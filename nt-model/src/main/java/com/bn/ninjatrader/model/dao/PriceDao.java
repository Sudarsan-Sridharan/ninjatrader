package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 4/30/16.
 */
public interface PriceDao {

  void save(final SavePriceRequest req);

  FindPricesOperation findPrices();

  Set<String> findAllSymbols();

  List<Price> findBeforeDate(final FindBeforeDateRequest build);

  /**
   * Builder interface for finding prices
   */
  interface FindPricesOperation {

    FindPricesOperation withSymbol(final String symbol);

    FindPricesOperation from(final LocalDate from);

    FindPricesOperation to(final LocalDate to);

    FindPricesOperation withTimeFrame(final TimeFrame timeFrame);

    List<Price> now();
  }
}
