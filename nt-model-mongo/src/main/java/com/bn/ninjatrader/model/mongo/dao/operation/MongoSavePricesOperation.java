package com.bn.ninjatrader.model.mongo.dao.operation;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.mongo.document.MongoPriceDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builder for finding prices operation
 */
public final class MongoSavePricesOperation implements PriceDao.SavePricesOperation {
  private static final Logger LOG = LoggerFactory.getLogger(MongoSavePricesOperation.class);

  private final MongoCollection mongoCollection;
  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private Collection<Price> prices = Collections.emptyList();

  public MongoSavePricesOperation(final MongoCollection mongoCollection, Collection<Price> prices) {
    this.mongoCollection = mongoCollection;
    this.prices = prices;
  }

  @Override
  public MongoSavePricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public MongoSavePricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public void now() {
    final Map<Integer, List<Price>> pricesByYear = prices.stream()
        .collect(Collectors.groupingBy(price -> price.getDate().getYear(), Collectors.toList()));

    final List<Integer> years = pricesByYear.keySet().stream().sorted().collect(Collectors.toList());

    final Map<Integer, MongoPriceDocument> docsByYear =
        Lists.newArrayList(mongoCollection
            .find(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR_RANGE, symbol, timeFrame, years.get(0), years.get(years.size()-1))
            .as(MongoPriceDocument.class)
            .iterator())
            .stream().collect(Collectors.toMap(MongoPriceDocument::getYear, d -> d));

    pricesByYear.keySet().stream().forEach(year -> {

      // If no document found for the year, create it
      if (!docsByYear.containsKey(year)) {
        docsByYear.put(year, new MongoPriceDocument(symbol, year, timeFrame));
      }

      final Map<LocalDate, Price> newPrices = pricesByYear.get(year).stream()
          .collect(Collectors.toMap(Price::getDate, price -> price));

      final MongoPriceDocument doc = docsByYear.get(year);
      final Map<LocalDate, Price> existingPrices = doc.getData().stream()
          .collect(Collectors.toMap(Price::getDate, d -> d));

      // Overwrite old prices w/ new
      existingPrices.putAll(newPrices);

      // Store merged prices to document and sort by date
      doc.setData(existingPrices.values().stream()
          .sorted(Comparator.comparing(Price::getDate))
          .collect(Collectors.toList()));

      saveDocument(doc);
    });
  }

  private void saveDocument(final MongoPriceDocument doc) {
    if (!prices.isEmpty()) {
      // Insert new values
      mongoCollection
          .update(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR, symbol, timeFrame, doc.getYear())
          .upsert()
          .with(doc);
    }
  }
}
