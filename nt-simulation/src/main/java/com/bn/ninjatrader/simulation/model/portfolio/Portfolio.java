package com.bn.ninjatrader.simulation.model.portfolio;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(Portfolio.class);

  private static final String DEFAULT = "DEFAULT";
  private static final String SYMBOL_NOT_EXIST_ERROR_MSG = "Portfolio doesn't contain stock symbol [%s].";

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
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    portfolioItems.put(symbol, portfolioItems.get(symbol).commitShares(shares));
  }

  public void cancelCommittedShares(final String symbol, final long shares) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    portfolioItems.put(symbol, portfolioItems.get(symbol).cancelCommittedShares(shares));
  }

  public void fulfillCommittedShares(final String symbol, final long shares) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    final PortfolioItem item = portfolioItems.get(symbol).fulfillCommittedShares(shares);
    if (item.getTotalShares() == 0) {
      portfolioItems.remove(item.getSymbol());
    } else {
      portfolioItems.put(symbol, portfolioItems.get(symbol).fulfillCommittedShares(shares));
    }
  }

  public boolean contains(final String symbol) {
    return portfolioItems.containsKey(symbol);
  }

  public long getCommittedShares(final String symbol) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    return portfolioItems.get(symbol).getCommittedShares();
  }

  public long getTotalShares() {
    return portfolioItems.values().stream()
        .collect(Collectors.summingLong(p -> p.getTotalShares()));
  }

  @Deprecated
  public long getTotalShares(final String symbol) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    return portfolioItems.get(symbol).getTotalShares();
  }

  public double getAvgPrice() {
    return portfolioItems.values().stream().collect(Collectors.summingDouble(p -> p.getAvgPrice()));
  }

  @Deprecated
  public double getAvgPrice(final String symbol) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    return portfolioItems.get(symbol).getAvgPrice();
  }

  public double getEquityValue() {
    return portfolioItems.values().stream()
        .collect(Collectors.summingDouble(p -> p.getEquityValue()));
  }

  @Deprecated
  public double getEquityValue(final String symbol) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    return portfolioItems.get(symbol).getEquityValue();
  }

  public double getTotalEquityValue(final BarData barData) {
    return portfolioItems.values().stream().mapToDouble(item -> item.getEquityValue(barData)).sum();
  }

  public double getTotalEquityValue() {
    return portfolioItems.values().stream().mapToDouble(item -> item.getEquityValue()).sum();
  }

  public boolean canCommitShares(final String symbol, final long shares) {
    checkArgument(portfolioItems.containsKey(symbol), SYMBOL_NOT_EXIST_ERROR_MSG, symbol);
    return portfolioItems.get(symbol).canCommitShares(shares);
  }

  public boolean isEmpty() {
    return portfolioItems.isEmpty();
  }

  public void clear() {
    portfolioItems.clear();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).addValue(portfolioItems).toString();
  }
}
