package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio {

  private static final Logger LOG = LoggerFactory.getLogger(Portfolio.class);

  private List<BuyTransaction> buyTransactions = Lists.newArrayList();
  private Map<String, PortfolioItem> portfolioItems = Maps.newHashMap();

  public void add(final BuyTransaction buyTnx) {
    checkNotNull(buyTnx, "buyTransaction must not be null.");

    final String symbol = buyTnx.getSymbol();

    buyTransactions.add(buyTnx);

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
    LOG.info("{}", portfolioItems.values().iterator().next().getAvgPrice());
    return portfolioItems.values().iterator().next().getAvgPrice();
  }

  public double getEquityValue() {
    return portfolioItems.values().iterator().next().getEquityValue();
  }

  public boolean isEmpty() {
    return portfolioItems.isEmpty();
  }

  public void clear() {
    portfolioItems.clear();
  }
}
