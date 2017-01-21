package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.data.BarDataHistory;
import com.bn.ninjatrader.simulation.data.SimulationData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation {

  private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

  private final List<Price> priceList;
  private final BarDataFactory barDataFactory;
  private final BarDataHistory barDataHistory = BarDataHistory.withMaxSize(52);
  private final SimulationParams simulationParams;

  private final Broker broker;
  private final Account account;

  private final List<Statement> statements;

  private int barIndex;

  public Simulation(final Account account,
                    final Broker broker,
                    final SimulationParams simulationParams,
                    final List<Price> priceList,
                    final BarDataFactory barDataFactory) {

    this.account = account;
    this.broker = broker;
    this.simulationParams = simulationParams;
    this.priceList = priceList;
    this.statements = simulationParams.getStatements();
    this.barDataFactory = barDataFactory;

    checkValidConstructorParams();
  }

  private void checkValidConstructorParams() {
    checkNotNull(simulationParams, "SimulationParams must not be null.");
    checkNotNull(account, "Account must not be null.");
    checkArgument(simulationParams.getStartingCash() > 0, "Starting cash must be > 0.");
    checkNotNull(priceList);
    checkArgument(priceList.size() > 0, "Price list must not be empty.");
  }

  public SimulationReport play() {
    barIndex = 0;
    for (final Price price : priceList) {
      final BarData barData = barDataFactory.createWithPriceAtIndex(price, barIndex);
      barDataHistory.add(barData);
      processBar(barData);
      barIndex++;
    }
    onSimulationEnd();
    return createSimulationReport();
  }

  private void processBar(final BarData barData) {
    for (final Statement statement : statements) {
      statement.run(this, barData);
    }
    broker.processPendingOrders(barData);
  }

  private void onSimulationEnd() {
    sellAll();
    account.print();
  }

  private void sellAll() {
    if (account.hasShares()) {
      int lastIndex = priceList.size() - 1;
      Price lastPrice = priceList.get(lastIndex);
      BarData barData = barDataFactory.createWithPriceAtIndex(lastPrice, lastIndex);
      broker.submitOrder(Order.sell().date(lastPrice.getDate()).build(), barData);
      broker.processPendingOrders(barData);
    }
  }

  public void addSimulationData(final Collection<SimulationData> dataList) {
    this.barDataFactory.addSimulationData(dataList);
  }

  public SimulationReport createSimulationReport() {
    final SimulationReport report = new SimulationReport();
    report.setSimulationParams(simulationParams);
    report.setTradeStatistic(account.getTradeStatistic());
    report.setTransactions(account.getBookkeeper().getTransactions());
    report.setEndingCash(account.getCash());
    return report;
  }

  public SimulationParams getSimulationParams() {
    return simulationParams;
  }

  public Account getAccount() {
    return account;
  }

  public Broker getBroker() {
    return broker;
  }

  public BarDataHistory getBarDataHistory() {
    return barDataHistory;
  }
}
