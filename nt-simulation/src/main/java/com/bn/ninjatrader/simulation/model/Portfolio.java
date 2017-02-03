package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio {
  private static final Logger LOG = LoggerFactory.getLogger(Portfolio.class);

  private Map<String, PortfolioItem> portfolioItems = Maps.newHashMap();

  public void add(final BuyTransaction buyTnx) {
    checkNotNull(buyTnx, "buyTransaction must not be null.");

    final String symbol = buyTnx.getSymbol();

    if (!portfolioItems.containsKey(symbol)) {
      portfolioItems.put(symbol, PortfolioItem.builder().symbol(symbol).build());
    }

    // Update portfolioItem
    final PortfolioItem portfolioItem = portfolioItems.remove(symbol);
    portfolioItems.put(symbol, portfolioItem.addSharesWithPrice(buyTnx.getNumOfShares(), buyTnx.getPrice()));
  }

  // TODO fix this to getTotalShares(symbol)
  public long getTotalShares() {
    if (isEmpty()) {
      return 0;
    }
    return portfolioItems.values().iterator().next().getTotalShares();
  }

  // TODO fix this to getAvPrice(symbol)
  public double getAvgPrice() {
    if (isEmpty()) {
      return 0.0;
    }
    return portfolioItems.values().iterator().next().getAvgPrice();
  }

  // TODO fix this to getEquityValue(symbol)
  public double getEquityValue() {
    if (isEmpty()) {
      return 0.0;
    }
    return portfolioItems.values().iterator().next().getEquityValue();
  }

  public boolean isEmpty() {
    return portfolioItems.isEmpty();
  }

  public void clear() {
    portfolioItems.clear();
  }
}
