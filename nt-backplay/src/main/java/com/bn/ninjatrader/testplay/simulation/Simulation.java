package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.condition.Condition;
import com.bn.ninjatrader.testplay.parameter.BarParameters;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation {

  private static final Logger log = LoggerFactory.getLogger(Simulation.class);

  private List<Price> priceList;
  private List<Ichimoku> ichimokuList;
  private final TestPlayParameters testPlayParameters;
  private final Broker broker;
  private final Account account;
  private final Condition buyCondition;
  private final Condition sellCondition;

  public Simulation(TestPlayParameters testPlayParameters, List<Price> priceList) {
    this.testPlayParameters = testPlayParameters;
    this.priceList = priceList;
    this.account = Account.startWithCash(testPlayParameters.getStartingEquity());
    this.buyCondition = testPlayParameters.getBuyCondition();
    this.sellCondition = testPlayParameters.getSellCondition();
    this.broker = new Broker(account);
    checkValidConstructorParams();
  }

  private void checkValidConstructorParams() {
    Preconditions.checkNotNull(testPlayParameters);
    Preconditions.checkNotNull(buyCondition);
    Preconditions.checkNotNull(sellCondition);
    Preconditions.checkNotNull(account);
    Preconditions.checkArgument(testPlayParameters.getStartingEquity() > 0, "Starting equity must be > 0.");
    Preconditions.checkNotNull(priceList);
    Preconditions.checkArgument(priceList.size() > 0, "Price list must not be empty.");
  }

  public void play() {
    BarParameters barParameters = new BarParameters();
    Iterator<Price> priceIter = priceList.iterator();
    Iterator<Ichimoku> ichimokuIter = ichimokuList.iterator();

    Price lastPrice = null;
    while (priceIter.hasNext()) {
      Price price = priceIter.next();
      Ichimoku ichimoku = ichimokuIter.next();

      barParameters.clearValues().put(price).put(ichimoku);
      processBar(barParameters);
      lastPrice = price;
    }

    broker.submitOrder(Order.sell().date(lastPrice.getDate()).build());
    account.printStats();
  }

  private void processBar(BarParameters barParameters) {
    if (account.getPortfolio().isEmpty()) {
      processBuy(barParameters);
    } else {
      processSell(barParameters);
    }
    broker.processPendingOrders(barParameters.getPrice());
  }

  private void processBuy(BarParameters barParameters) {
    if (buyCondition.isMatch(barParameters)) {
      Price price = barParameters.getPrice();
      Order order = Order.buy().cashAmount(account.getCash())
          .date(price.getDate())
          .build();
      broker.submitOrder(order);
    }
  }

  private void processSell(BarParameters barParameters) {
    if (sellCondition.isMatch(barParameters)) {
      Price price = barParameters.getPrice();
      Order order = Order.sell()
          .date(price.getDate())
          .build();
      broker.submitOrder(order);
    }
  }

  public void setIchimokuList(List<Ichimoku> ichimokuList) {
    this.ichimokuList = alignToPrices(ichimokuList, Ichimoku.empty());
  }

  private <T> List<T> alignToPrices(List<T> dataList, T emptyFill) {
    if (dataList == null) {
      return dataList;
    }

    for (int i = dataList.size(); i < priceList.size(); i++) {
      dataList.add(0, emptyFill);
    }
    return dataList;
  }
}
