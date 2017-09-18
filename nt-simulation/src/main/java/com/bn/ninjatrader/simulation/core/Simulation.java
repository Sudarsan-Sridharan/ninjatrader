package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.bn.ninjatrader.simulation.algorithm.ScriptRunner;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarProducer;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.order.CleanupOrder;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation implements BrokerListener, Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

  private final BarProducer barProducer;
  private final SimulationRequest simRequest;
  private final SimulationContext simulationContext;
  private final Broker broker;
  private final Account account;
  private final History history;
  private final Map<String, List<Price>> priceDatastore;
  private final AlgorithmScript algorithmScript;
  private ScriptRunner scriptRunner;

  private Simulation() {
    this.simulationContext = null;
    this.account = null;
    this.broker = null;
    this.priceDatastore = null;
    this.history = null;
    this.simRequest = null;
    this.barProducer = null;
    this.algorithmScript = null;
    this.scriptRunner = null;
  }

  public Simulation(final SimulationContext simulationContext,
                    final SimulationRequest simRequest,
                    final BarProducer barProducer) {
    checkNotNull(simRequest, "SimRequest must not be null.");
    checkNotNull(simulationContext, "SimContext must not be null.");
    checkNotNull(simulationContext.getAccount(), "World.Account must not be null.");
    checkNotNull(simulationContext.getPrices(), "World.Prices must not be null.");

    this.simulationContext = simulationContext;
    this.account = simulationContext.getAccount();
    this.broker = simulationContext.getBroker();
    this.priceDatastore = simulationContext.getPrices();
    this.history = simulationContext.getHistory();
    this.simRequest = simRequest;
    this.barProducer = barProducer;
    this.algorithmScript = simRequest.getAlgorithmScript();

    broker.addListeners(this, account);
  }

  public SimulationReport play() {
    this.scriptRunner = this.algorithmScript.newRunner();
    BarData bar = null;
    try {
      scriptRunner.onSimulationStart(this.simulationContext);

      for (final Map.Entry<String, List<Price>> entry : priceDatastore.entrySet()) {
        final String symbol = entry.getKey();
        for (final Price price : entry.getValue()) {
          bar = barProducer.nextBar(symbol, price, simulationContext);
          processBar(bar);
        }
      }
      onSimulationEnd(bar);
      return createSimulationReport(bar).build();
    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
      return createSimulationReport(e, bar).build();
    }
  }

  private void processBar(final BarData bar) {
    history.add(bar);
    broker.setCurrentBar(bar);
    broker.processPendingOrders(bar);
    scriptRunner.processBar(bar);
    broker.processPendingOrders(bar);
  }

  private void onSimulationEnd(final BarData bar) {
    scriptRunner.onSimulationEnd();

    if (account.getPortfolio().getTotalShares() > 0) {
      broker.submitOrder(CleanupOrder.instance(), bar);
      broker.processPendingOrders(bar);
    }
  }

  public SimulationReport.Builder createSimulationReport(final BarData bar) {
    return SimulationReport.builder()
        .symbol(simRequest.getSymbol())
        .tradeStatistics(account.getTradeStatistic())
        .addTransactions(account.getTradeStatistic().getTransactions())
        .startingCash(simRequest.getStartingCash())
        .endingCash(account.getLiquidCash())
        .profit(account.getProfit())
        .profitPcnt(NumUtil.divide(account.getProfit(), simRequest.getStartingCash()))
        .addMarks(simulationContext.getChartMarks())
        .addBrokerLogs(broker.getLogs());
  }

  public SimulationReport.Builder createSimulationReport(final Exception e, final BarData bar) {
    return createSimulationReport(bar).error(e.getMessage());
  }

  public Account getAccount() {
    return account;
  }

  public Broker getBroker() {
    return broker;
  }

  public SimulationContext getSimulationContext() {
    return simulationContext;
  }

  @Override
  public void onFulfilledBuy(final BuyTransaction transaction, final BarData bar) {
    scriptRunner.onBuyFulfilled(transaction, bar);
  }

  @Override
  public void onFulfilledSell(final SellTransaction transaction, final BarData bar) {
    scriptRunner.onSellFulfilled(transaction, bar);
  }

  @Override
  public void onCleanup(final SellTransaction transaction, final BarData bar) {
    // Ignore
  }
}
