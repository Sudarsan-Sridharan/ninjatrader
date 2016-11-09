package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.SellOrderParameters;
import com.bn.ninjatrader.simulation.condition.Condition;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.order.BuyOrderParameters;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation {

  private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

  private List<Price> priceList;
  private BarDataFactory barDataFactory = new BarDataFactory();
  private SimulationParams simulationParams;

  private Broker broker;
  private Account account;

  private Condition buyCondition;
  private Condition sellCondition;

  private BuyOrderParameters buyOrderParams;
  private SellOrderParameters sellOrderParams;

  public Simulation(Account account,
                    Broker broker,
                    SimulationParams simulationParams,
                    List<Price> priceList) {

    this.account = account;
    this.broker = broker;
    this.simulationParams = simulationParams;
    this.priceList = priceList;

    this.buyCondition = simulationParams.getBuyCondition();
    this.buyOrderParams = simulationParams.getBuyOrderParams();

    this.sellCondition = simulationParams.getSellCondition();
    this.sellOrderParams = simulationParams.getSellOrderParams();

    checkValidConstructorParams();
  }

  private void checkValidConstructorParams() {
    Preconditions.checkNotNull(simulationParams);
    Preconditions.checkNotNull(buyCondition);
    Preconditions.checkNotNull(buyOrderParams);
    Preconditions.checkNotNull(sellCondition);
    Preconditions.checkNotNull(sellOrderParams);
    Preconditions.checkNotNull(account);
    Preconditions.checkArgument(simulationParams.getStartingCash() > 0, "Starting equity must be > 0.");
    Preconditions.checkNotNull(priceList);
    Preconditions.checkArgument(priceList.size() > 0, "Price list must not be empty.");
  }

  public SimulationReport play() {
    for (Price price : priceList) {
      BarData barData = barDataFactory.create(price);
      processBar(barData);
    }
    onSimulationEnd();
    return createSimulationReport();
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
          .params(buyOrderParams)
          .build();
      broker.submitOrder(order);
    }
  }

  private void processSell(BarData barData) {
    if (sellCondition.isMatch(barData)) {
      Price price = barData.getPrice();
      Order order = Order.sell()
          .date(price.getDate())
          .params(sellOrderParams)
          .build();
      broker.submitOrder(order);
    }
  }

  private void onSimulationEnd() {
    sellAll();
    account.print();
  }

  private void sellAll() {
    int lastIndex = priceList.size() - 1;
    Price lastPrice = priceList.get(lastIndex);
    broker.submitOrder(Order.sell().date(lastPrice.getDate()).build());
    BarData barData = barDataFactory.create(lastPrice, lastIndex);
    broker.processPendingOrders(barData);
  }

  public void addSimulationData(List<SimulationData> dataList) {
    this.barDataFactory.addSimulationData(dataList);
  }

  public SimulationReport createSimulationReport() {
    SimulationReport report = new SimulationReport();
    report.setSimulationParams(simulationParams);
    report.setTradeStatistic(account.getTradeStatistic());
    report.setTransactions(account.getBookkeeper().getTransactions());
    report.setEndingCash(account.getCash());
    return report;
  }
}
