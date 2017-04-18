package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.util.DateObjUtil;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Builder for finding prices operation
 */
public final class DatastoreFindPricesOperation implements PriceDao.FindPricesOperation {
  private String symbol;
  private LocalDate from;
  private LocalDate to;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  @Override
  public DatastoreFindPricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation from(final LocalDate from) {
    this.from = from;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation to(final LocalDate to) {
    this.to = to;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public List<Price> now() {
    final int fromYear = from.getYear();
    final int toYear = to.getYear();

    // Create keys for each year
    final List<Key<PriceDocument>> keys = Lists.newArrayList();
    for (int i = fromYear; i <= toYear; i++) {
      keys.add(Key.create(PriceDocument.class, PriceDocument.id(symbol, i, timeFrame)));
    }

    // Find documents
    final Map<Key<PriceDocument>, PriceDocument> documents = ofy().load().keys(keys);

    // Collect prices from each document
    final List<Price> prices = documents.values()
        .stream().flatMap(doc -> doc.getData().stream())
        .sorted()
        .collect(Collectors.toList());

    DateObjUtil.trimToDateRange(prices, from, to);

    return prices;
  }
}
