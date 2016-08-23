package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.condition.Condition;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.BarDataHistory;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import com.bn.ninjatrader.testplay.simulation.order.MarketTime;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
  private BarDataHistory barDataHistory = BarDataHistory.withMaxSize(52);
  private SimulationParameters simulationParameters;

  private Broker broker;
  private Account account;

  private Condition buyCondition;
  private Condition sellCondition;

  public Simulation(Account account,
                    Broker broker,
                    SimulationParameters simulationParameters,
                    List<Price> priceList) {
    this.account = account;
    this.broker = broker;
    this.simulationParameters = simulationParameters;
    this.priceList = priceList;
    this.buyCondition = simulationParameters.getBuyCondition();
    this.sellCondition = simulationParameters.getSellCondition();
    this.buyCondition = simulationParameters.getBuyCondition();
    this.sellCondition = simulationParameters.getSellCondition();

    checkValidConstructorParams();
  }

  private void checkValidConstructorParams() {
    Preconditions.checkNotNull(simulationParameters);
    Preconditions.checkNotNull(buyCondition);
    Preconditions.checkNotNull(sellCondition);
    Preconditions.checkNotNull(account);
    Preconditions.checkArgument(simulationParameters.getStartingCash() > 0, "Starting equity must be > 0.");
    Preconditions.checkNotNull(priceList);
    Preconditions.checkArgument(priceList.size() > 0, "Price list must not be empty.");
  }

  public void play() {
    int index = 0;
    for (Price price : priceList) {
      BarData barData = setupBarData(price, index);
      processBar(barData);
      barDataHistory.add(barData);
      index++;
    }

    Price lastPrice = priceList.get(priceList.size()-1);
    broker.submitOrder(Order.sell().date(lastPrice.getDate()).build());
    BarData barData = setupBarData(lastPrice, index-1);
    broker.processPendingOrders(barData);
    account.print();
  }

  private BarData setupBarData(Price price, int index) {
    BarData barData = new BarData();
    barData.index(index).put(price).put(broker);
    for (SimulationData data : dataList) {
      DataMap dataMap = data.getDataMap(index);
      barData.put(dataMap);
    }
    return barData;
  }

  private void processBar(BarData barData) {
    if (account.hasShares()) {
      processSell(barData);
    } else if (!broker.hasPendingOrder()) {
      processBuy(barData);
    }
    broker.processPendingOrders(barData);
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

  public void addSimulationData(List<SimulationData> dataList) {
    this.dataList.addAll(dataList);
  }
}
