package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.model.*;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.Statement;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation implements BrokerListener {

  private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

  private final List<SimulationData> simulationDataList = Lists.newArrayList();
  private final BarDataFactory barDataFactory;
  private final SimulationParams simulationParams;

  private final World world;
  private final Broker broker;
  private final Account account;
  private final History history;
  private final LocalProperties properties;
  private final Map<String, List<Price>> priceDatastore;

  private final List<Statement> statements;

  private int barIndex;

  public Simulation(final World world,
                    final SimulationParams simulationParams,
                    final BarDataFactory barDataFactory) {
    checkNotNull(simulationParams, "SimulationParams must not be null.");
    checkNotNull(world, "world must not be null.");
    checkNotNull(world.getAccount(), "World.Account must not be null.");
    checkNotNull(world.getPrices(), "World.Prices must not be null.");
    checkArgument(world.getPrices().size() > 0, "World.Prices must not be empty.");
    checkArgument(simulationParams.getStartingCash() > 0, "Starting cash must be > 0.");

    this.world = world;
    this.account = world.getAccount();
    this.broker = world.getBroker();
    this.priceDatastore = world.getPrices();
    this.history = world.getHistory();
    this.properties = world.getProperties();
    this.simulationParams = simulationParams;
    this.statements = simulationParams.getStatements();
    this.barDataFactory = barDataFactory;

    broker.addListeners(this, account);
  }

  public SimulationReport play() {
    barIndex = 0;
    for (final Map.Entry<String, List<Price>> entry : priceDatastore.entrySet()) {
      final String symbol = entry.getKey();
      for (final Price price : entry.getValue()) {
        final BarData barData = barDataFactory.create(symbol, price, barIndex, simulationDataList, world);
        history.add(barData);
        processBar(barData);
        barIndex++;
      }
    }
    onSimulationEnd();
    return createSimulationReport();
  }

  private void processBar(final BarData barData) {
    for (final Statement statement : statements) {
      statement.run(barData);
    }
    broker.processPendingOrders(barData);
  }

  private void onSimulationEnd() {

  }

  private void sellAll(final String symbol) {
    if (!account.getPortfolio().isEmpty()) {
      final List<Price> priceList = priceDatastore.get(symbol);
      int lastIndex = priceList.size() - 1;
      final Price lastPrice = priceList.get(lastIndex);
      final BarData barData = barDataFactory.create(symbol, lastPrice, lastIndex, simulationDataList, world);
      broker.submitOrder(SellOrder.builder().date(lastPrice.getDate()).build(), barData);
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
    report.setStartingCash(simulationParams.getStartingCash());
    report.setEndingCash(account.getTotalAccountValue());
    report.setMarks(world.getChartMarks());
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

  public World getWorld() {
    return world;
  }

  @Override
  public void onFulfilledBuy(final BuyTransaction transaction, final BarData barData) {
    simulationParams.getOnBuyFulfilledStatement().run(barData);
  }

  @Override
  public void onFulfilledSell(final SellTransaction transaction, final BarData barData) {
    simulationParams.getOnSellFulfilledStatement().run(barData);
  }
}
