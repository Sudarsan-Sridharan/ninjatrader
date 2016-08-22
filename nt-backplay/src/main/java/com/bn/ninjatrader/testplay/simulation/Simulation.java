package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.condition.Condition;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import com.bn.ninjatrader.testplay.simulation.order.MarketTime;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation {

  private static final Logger log = LoggerFactory.getLogger(Simulation.class);

  private List<Price> priceList;
  private List<SimulationData> dataList = Lists.newArrayList();
  private TestPlayParameters testPlayParameters;

  @Inject
  private Broker broker;

  @Inject
  private Account account;

  private Condition buyCondition;
  private Condition sellCondition;

  public Simulation() {

  }

  public void setup(TestPlayParameters testPlayParameters, List<Price> priceList) {
    this.testPlayParameters = testPlayParameters;
    this.priceList = priceList;
    //this.account = Account.startWithCash(testPlayParameters.getStartingEquity());
    this.buyCondition = testPlayParameters.getBuyCondition();
    this.sellCondition = testPlayParameters.getSellCondition();
    //this.broker = new Broker(account);

    log.info("ACCOUNT: {}", account);
    log.info("BROKER: {}", broker);
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
    checkValidConstructorParams();

    BarData barData = new BarData();

    int index = 0;
    for (Price price : priceList) {
      fillBarData(barData, price, index);
      processBar(barData);
      index++;
    }

    Price lastPrice = priceList.get(priceList.size()-1);
    broker.submitOrder(Order.sell().date(lastPrice.getDate()).build());
    broker.processPendingOrders(lastPrice);

    account.print();
  }

  private void fillBarData(BarData barData, Price price, int index) {
    barData.clear().put(price);
    for (SimulationData data : dataList) {
      DataMap dataMap = data.getDataMap(index);
      barData.put(dataMap);
    }
  }

  private void processBar(BarData barData) {
    if (account.hasShares()) {
      processSell(barData);
    } else if (!account.hasPendingOrder()) {
      processBuy(barData);
    }
    broker.processPendingOrders(barData.getPrice());
  }

  private void processBuy(BarData barData) {
    if (buyCondition.isMatch(barData)) {
      Price price = barData.getPrice();
      Order order = Order.buy()
          .date(price.getDate())
          .cashAmount(account.getCash())
          .daysFromNow(1)
          .at(MarketTime.OPEN)
          .build();
      broker.submitOrder(order);
      account.addPendingOrder(order);
    }
  }

  private void processSell(BarData barData) {
    if (sellCondition.isMatch(barData)) {
      Price price = barData.getPrice();
      Order order = Order.sell()
          .date(price.getDate())
          .build();
      broker.submitOrder(order);
    }
  }

  public void addSimulationData(SimulationData data) {
    this.dataList.add(data);
  }
}
