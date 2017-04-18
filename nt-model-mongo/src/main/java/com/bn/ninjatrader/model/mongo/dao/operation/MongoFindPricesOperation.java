package com.bn.ninjatrader.model.mongo.dao.operation;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.mongo.document.MongoPriceDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder for finding prices operation
 */
public final class MongoFindPricesOperation implements PriceDao.FindPricesOperation {
  private final MongoCollection mongoCollection;
  private String symbol;
  private LocalDate from;
  private LocalDate to;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  public MongoFindPricesOperation(final MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  @Override
  public MongoFindPricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public MongoFindPricesOperation from(final LocalDate from) {
    this.from = from;
    return this;
  }

  @Override
  public MongoFindPricesOperation to(final LocalDate to) {
    this.to = to;
    return this;
  }

  @Override
  public MongoFindPricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public List<Price> now() {
    try (final MongoCursor<MongoPriceDocument> cursor = mongoCollection
        .find(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR_RANGE, symbol,
            timeFrame,
            from.getYear(),
            to.getYear())
        .as(MongoPriceDocument.class)) {

      final List<Price> prices = Lists.newArrayList(cursor.iterator()).stream()
          .flatMap(doc -> doc.getData().stream())
          .filter(d -> !d.getDate().isBefore(from) && !d.getDate().isAfter(to))
          .sorted()
          .collect(Collectors.toList());

      return prices;
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
