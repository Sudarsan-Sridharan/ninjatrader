package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 4/30/16.
 */
public interface PriceDao {

  SavePricesOperation savePrices(final Collection<Price> prices);

  SavePricesOperation savePrices(final Price price, final Price ... more);

  FindPricesOperation findPrices();

  Set<String> findAllSymbols();

  List<Price> findBeforeDate(final FindBeforeDateRequest build);

  RenameSymbolOperation renameSymbol(final String symbol);

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

  /**
   * Builder interface for saving prices
   */
  interface SavePricesOperation {

    SavePricesOperation withSymbol(final String symbol);

    SavePricesOperation withTimeFrame(final TimeFrame timeFrame);

    void now();
  }

  /**
   * Builder interface for renaming stock symbols
   */
  interface RenameSymbolOperation {

    RenameSymbolOperation to(final String symbol);

    void now();
  }
}
