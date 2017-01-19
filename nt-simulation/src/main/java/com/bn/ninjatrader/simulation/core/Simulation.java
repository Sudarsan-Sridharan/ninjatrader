package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
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

  private List<Price> priceList;
  private BarDataFactory barDataFactory = new BarDataFactory();
  private SimulationParams simulationParams;

  private Broker broker;
  private Account account;

  private List<Statement> statements;

  public Simulation(final Account account,
                    final Broker broker,
                    final SimulationParams simulationParams,
                    final List<Price> priceList) {

    this.account = account;
    this.broker = broker;
    this.simulationParams = simulationParams;
    this.priceList = priceList;
    this.statements = simulationParams.getStatements();

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
    for (final Price price : priceList) {
      final BarData barData = barDataFactory.create(price);
      processBar(barData);
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
      broker.submitOrder(Order.sell().date(lastPrice.getDate()).build());
      BarData barData = barDataFactory.create(lastPrice, lastIndex);
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
}
