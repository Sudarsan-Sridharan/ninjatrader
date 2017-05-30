package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Script Runner
 */
public class GroovyScriptRunner implements ScriptRunner {
  private static final String ON_SIM_START_METHOD = "onSimulationStart";
  private static final String ON_SIM_END_METHOD = "onSimulationEnd";
  private static final String PROCESS_BAR_METHOD = "processBar";
  private static final String ON_BUY_FULFILLED_METHOD = "onBuyFulfilled";
  private static final String ON_SELL_FULFILLED_METHOD = "onSellFulfilled";

  private final Map<String, Object> varBindings = Maps.newHashMap();
  private final Set<ScriptVariable> historicalVariables;
  private final Binding binding = new Binding(varBindings);
  private final Script script;
  private final Logger scriptLogger;

  GroovyScriptRunner(final Script script, Set<ScriptVariable> historicalVariables) {
    this.script = script;
    this.script.setBinding(binding);
    this.scriptLogger = LoggerFactory.getLogger(script.getClass());
    this.historicalVariables = historicalVariables;
  }

  @Override
  public void onSimulationStart(final SimulationContext context) {
    varBindings.put(DataType.LOG, this.scriptLogger);
    varBindings.put(DataType.BROKER, context.getBroker());
    varBindings.put(DataType.HISTORY, context.getHistory());
    varBindings.put(DataType.MARKERS, context.getChartMarks());
    varBindings.put(DataType.BOARDLOT, context.getBoardLotTable());
    varBindings.put(DataType.ACCOUNT, context.getAccount());
    varBindings.put(DataType.PORTFOLIO, context.getAccount().getPortfolio());

    script.invokeMethod(ON_SIM_START_METHOD, null);
  }

  @Override
  public void processBar(final BarData bar) {
    script.run();
    varBindings.putAll(bar.getDataMap().getBindings());

    varBindings.put(DataType.SYMBOL, bar.getSymbol());

    final History history = bar.getSimulationContext().getHistory();

    for (final ScriptVariable historicalVariable : historicalVariables) {
      final Optional<BarData> historyBar = history.getNBarsAgo(historicalVariable.getBarsAgo());
      if (historyBar.isPresent()) {
        varBindings.put(historicalVariable.getSafeName(), historyBar.get().get(historicalVariable.getVariable()));
      } else {
        varBindings.put(historicalVariable.getSafeName(), 0d);
      }
    }

    script.invokeMethod(PROCESS_BAR_METHOD, null);
  }

  @Override
  public void onBuyFulfilled(final BuyTransaction txn, BarData barData) {
    script.invokeMethod(ON_BUY_FULFILLED_METHOD,
        new Object[] {txn.getDate(), txn.getPrice(), txn.getNumOfShares()});
  }

  @Override
  public void onSellFulfilled(final SellTransaction txn, BarData barData) {
    script.invokeMethod(ON_SELL_FULFILLED_METHOD,
        new Object[] {txn.getDate(), txn.getPrice(), txn.getNumOfShares()});
  }

  @Override
  public void onSimulationEnd() {
    script.invokeMethod(ON_SIM_END_METHOD, null);
  }
}
