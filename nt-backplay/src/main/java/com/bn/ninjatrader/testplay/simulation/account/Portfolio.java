package com.bn.ninjatrader.testplay.simulation.account;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio {

  private static final Logger log = LoggerFactory.getLogger(Portfolio.class);

  private List<Order> orders = Lists.newArrayList();

  public boolean isEmpty() {
    return orders.isEmpty();
  }

  public void add(Order order) {
    orders.add(order);
  }

  public long getTotalShares() {
    long totalShares = 0;
    for (Order order : orders) {
      totalShares += order.getNumOfShares();
    }
    return totalShares;
  }

  public double getAvgPrice() {
    double totalValue = 0;
    double totalShares = 0;
    for (Order order : orders) {
      totalValue += order.getValue();
      totalShares += order.getNumOfShares();
    }
    return NumUtil.divide(totalValue, totalShares);
  }

  public void clear() {
    orders.clear();
  }
}
