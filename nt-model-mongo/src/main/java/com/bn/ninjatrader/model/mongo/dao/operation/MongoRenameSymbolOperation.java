package com.bn.ninjatrader.model.mongo.dao.operation;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.mongo.dao.MongoPriceDao;
import com.bn.ninjatrader.model.mongo.document.MongoPriceDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

/**
 * Builder for finding prices operation
 */
public final class MongoRenameSymbolOperation implements PriceDao.RenameSymbolOperation {
  private static final Logger LOG = LoggerFactory.getLogger(MongoRenameSymbolOperation.class);
  private final MongoPriceDao priceDao;
  private String symbol;
  private String newSymbol;

  public MongoRenameSymbolOperation(final MongoPriceDao priceDao, String symbol) {
    this.priceDao = priceDao;
    this.symbol = symbol;
  }

  @Override
  public MongoRenameSymbolOperation to(String symbol) {
    this.newSymbol = symbol;
    return this;
  }

  @Override
  public void now() {
    checkState(!StringUtils.isEmpty(symbol), "Old symbol is required.");
    checkState(!StringUtils.isEmpty(newSymbol), "New symbol is required.");

    // Merge prices from new symbol to old.
    for (final TimeFrame timeFrame : TimeFrame.values()) {
      final List<MongoPriceDocument> docs = Lists.newArrayList(priceDao.getMongoCollection()
          .find(Queries.FIND_BY_SYMBOL_TIMEFRAME, newSymbol, timeFrame)
          .as(MongoPriceDocument.class).iterator());

      final List<Price> prices = docs.stream()
          .flatMap(doc -> doc.getData().stream())
          .sorted()
          .collect(Collectors.toList());

      priceDao.savePrices(prices).withSymbol(symbol).withTimeFrame(timeFrame).now();
    }

    // Remove all prices from new symbol
    priceDao.getMongoCollection().remove(Queries.FIND_BY_SYMBOL, newSymbol);

    // Rename all old to new symbol
    for (final MongoPriceDocument doc :
        priceDao.getMongoCollection().find(Queries.FIND_BY_SYMBOL, symbol).as(MongoPriceDocument.class)) {
      doc.setSymbol(newSymbol);
      priceDao.getMongoCollection().save(doc);
    }
  }
}
