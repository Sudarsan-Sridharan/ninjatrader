package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarProducer;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.SimContext;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.bn.ninjatrader.simulation.algorithm.ScriptRunner;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation implements BrokerListener {

  private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

  private final BarProducer barProducer;
  private final SimulationRequest simRequest;

  private final SimContext simContext;
  private final Broker broker;
  private final Account account;
  private final History history;
  private final Map<String, List<Price>> priceDatastore;
  private final AlgorithmScript algorithmScript;
  private final ScriptRunner scriptRunner;

  public Simulation(final SimContext simContext,
                    final SimulationRequest simRequest,
                    final BarProducer barProducer) {
    checkNotNull(simRequest, "SimParams must not be null.");
    checkNotNull(simContext, "SimContext must not be null.");
    checkNotNull(simContext.getAccount(), "World.Account must not be null.");
    checkNotNull(simContext.getPrices(), "World.Prices must not be null.");

    this.simContext = simContext;
    this.account = simContext.getAccount();
    this.broker = simContext.getBroker();
    this.priceDatastore = simContext.getPrices();
    this.history = simContext.getHistory();
    this.simRequest = simRequest;
    this.barProducer = barProducer;
    this.algorithmScript = simRequest.getAlgorithmScript();
    this.scriptRunner = this.algorithmScript.newRunner();

    broker.addListeners(this, account);
  }

  public SimulationReport play() {
    try {
      scriptRunner.onSimulationStart(this.simContext);

      for (final Map.Entry<String, List<Price>> entry : priceDatastore.entrySet()) {
        final String symbol = entry.getKey();
        for (final Price price : entry.getValue()) {
          final BarData bar = barProducer.nextBar(symbol, price, simContext);
          processBar(bar);
        }
      }
      onSimulationEnd();
    } catch (final Exception e) {
      return createSimulationReport(e).build();
    }

    return createSimulationReport().build();
  }

  private void processBar(final BarData bar) {
    history.add(bar);
    broker.setCurrentBar(bar);
    scriptRunner.processBar(bar);
    broker.processPendingOrders(bar);
  }

  private void onSimulationEnd() {
    scriptRunner.onSimulationEnd();
  }

  public SimulationReport.Builder createSimulationReport() {
    return SimulationReport.builder()
        .symbol(simRequest.getSymbol())
        .tradeStatistics(account.getTradeStatistic())
        .addTransactions(account.getBookkeeper().getTransactions())
        .startingCash(simRequest.getStartingCash())
        .endingCash(account.getTotalAccountValue())
        .addMarks(simContext.getChartMarks())
        .addBrokerLogs(broker.getLogs());
  }

  public SimulationReport.Builder createSimulationReport(final Exception e) {
    return createSimulationReport().error(e.getMessage());
  }

  public Account getAccount() {
    return account;
  }

  public Broker getBroker() {
    return broker;
  }

  public SimContext getSimContext() {
    return simContext;
  }

  @Override
  public void onFulfilledBuy(final BuyTransaction transaction, final BarData barData) {
    scriptRunner.onBuyFulfilled(barData);
  }

  @Override
  public void onFulfilledSell(final SellTransaction transaction, final BarData barData) {
    scriptRunner.onSellFulfilled(barData);
  }
}
