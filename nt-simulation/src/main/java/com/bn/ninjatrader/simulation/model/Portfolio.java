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

  private static final String DEFAULT = "DEFAULT";

  private Map<String, PortfolioItem> portfolioItems = Maps.newHashMap();


  public void add(final BuyTransaction buyTnx) {
    checkNotNull(buyTnx, "buyTransaction must not be null.");

    // TODO set to DEFAULT for now til multi stock is supported
    final String symbol = buyTnx.getSymbol() == null ? DEFAULT : buyTnx.getSymbol() ;

    if (!portfolioItems.containsKey(symbol)) {
      portfolioItems.put(symbol, PortfolioItem.builder().symbol(symbol).build());
    }

    // Update portfolioItem
    final PortfolioItem portfolioItem = portfolioItems.remove(symbol);
    portfolioItems.put(symbol, portfolioItem.addSharesWithPrice(buyTnx.getNumOfShares(), buyTnx.getPrice()));
  }

  public void commitShares(final String symbol, final long shares) {
    if (portfolioItems.containsKey(symbol)) {
      portfolioItems.put(symbol, portfolioItems.get(symbol).commitShares(shares));

    //TODO for now we don't support multiple symbols.
    } else if (portfolioItems.containsKey(DEFAULT)) {
      portfolioItems.put(DEFAULT, portfolioItems.get(DEFAULT).commitShares(shares));
    }
  }

  public void cancelCommittedShares(final String symbol, final long shares) {
    if (portfolioItems.containsKey(symbol)) {
      portfolioItems.put(symbol, portfolioItems.get(symbol).cancelCommittedShares(shares));

      //TODO for now we don't support multiple symbols.
    } else if (portfolioItems.containsKey(DEFAULT)) {
      portfolioItems.put(DEFAULT, portfolioItems.get(DEFAULT).cancelCommittedShares(shares));
    }
  }

  public void fulfillCommittedShares(final String symbol, final long shares) {
    if (portfolioItems.containsKey(symbol)) {
      portfolioItems.put(symbol, portfolioItems.get(symbol).fulfillCommittedShares(shares));

      //TODO for now we don't support multiple symbols.
    } else if (portfolioItems.containsKey(DEFAULT)) {
      portfolioItems.put(DEFAULT, portfolioItems.get(DEFAULT).fulfillCommittedShares(shares));
    }
  }

  public long getCommittedShares(final String symbol) {
    if (portfolioItems.containsKey(symbol)) {
      return portfolioItems.get(symbol).getCommittedShares();
    }

    //TODO for now we don't support multiple symbols.
    if (portfolioItems.containsKey(DEFAULT)) {
      return portfolioItems.get(DEFAULT).getCommittedShares();
    }
    return 0;
  }

  public long getTotalShares(final String symbol) {
    if (portfolioItems.containsKey(symbol)) {
      return portfolioItems.get(symbol).getTotalShares();
    }
    return portfolioItems.get(DEFAULT).getTotalShares();
  }

  // TODO fix this to getTotalShares(symbol)
  public long getTotalShares() {
    if (!portfolioItems.containsKey(DEFAULT)) {
      return 0;
    }
    return portfolioItems.get(DEFAULT).getTotalShares();
  }

  // TODO fix this to getAvPrice(symbol)
  public double getAvgPrice() {
    if (!portfolioItems.containsKey(DEFAULT)) {
      return 0;
    }
    return portfolioItems.get(DEFAULT).getAvgPrice();
  }

  // TODO fix this to getEquityValue(symbol)
  public double getEquityValue() {
    if (!portfolioItems.containsKey(DEFAULT)) {
      return 0;
    }
    return portfolioItems.get(DEFAULT).getEquityValue();
  }

  public boolean canCommitShares(final String symbol, final long shares) {
    if (portfolioItems.containsKey(symbol)) {
      return portfolioItems.get(symbol).canCommitShares(shares);
    }
    return portfolioItems.get(DEFAULT).canCommitShares(shares);
  }

  public boolean isEmpty() {
    return portfolioItems.isEmpty();
  }

  public void clear() {
    portfolioItems.clear();
  }
}
