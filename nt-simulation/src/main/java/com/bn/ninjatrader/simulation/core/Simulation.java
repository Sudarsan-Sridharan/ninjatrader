package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.Statement;
import com.google.common.collect.Lists;
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

  private final List<SimulationData> simulationDataList = Lists.newArrayList();
  private final BarDataFactory barDataFactory;
  private final SimulationParams simulationParams;

  private final World world;
  private final Broker broker;
  private final Account account;
  private final History history;
  private final List<Price> priceList;

  private final List<Statement> statements;

  private int barIndex;

  public Simulation(final World world,
                    final SimulationParams simulationParams,
                    final BarDataFactory barDataFactory) {
    this.world = world;
    this.account = world.getAccount();
    this.broker = world.getBroker();
    this.priceList = world.getPrices();
    this.history = world.getHistory();
    this.simulationParams = simulationParams;
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
      final BarData barData = barDataFactory.createWithPriceAtIndex(price, barIndex, simulationDataList, history);
      history.add(barData);
      processBar(barData);
      barIndex++;
    }
    onSimulationEnd();
    return createSimulationReport();
  }

  private void processBar(final BarData barData) {
    for (final Statement statement : statements) {
      statement.run(world, barData);
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
      BarData barData = barDataFactory.createWithPriceAtIndex(lastPrice, lastIndex, simulationDataList, history);
      broker.submitOrder(Order.sell().date(lastPrice.getDate()).build(), barData);
      broker.processPendingOrders(barData);
    }
  }

  public void addSimulationData(final Collection<SimulationData> dataList) {
    this.simulationDataList.addAll(dataList);
  }

  public void addSimulationData(final SimulationData simulationData) {
    this.simulationDataList.add(simulationData);
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
