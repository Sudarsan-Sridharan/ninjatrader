package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.exception.ScriptCompileErrorException;
import com.bn.ninjatrader.simulation.model.SimContext;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bradwee2000@gmail.com
 */
public class GroovyAlgorithmScript implements AlgorithmScript {
  private static final Logger LOG = LoggerFactory.getLogger(GroovyAlgorithmScript.class);
  private static final Pattern varPattern = Pattern.compile("\\$\\w+");

  private static final GroovyShell shell = new GroovyShell();

  private final Set<Variable> variables;
  private final Class scriptClass;

  public GroovyAlgorithmScript(final String scriptText) {
    variables = Collections.unmodifiableSet(parseVariables(scriptText));

    try {
      // Cache the class so we don't keep recompiling script.
      scriptClass = shell.getClassLoader().parseClass(scriptText);
    } catch (final MultipleCompilationErrorsException e) {
      throw new ScriptCompileErrorException(e);
    }
  }

  @Override
  public ScriptRunner newRunner() {
    try {
      final Script script = (Script) scriptClass.newInstance();
      return new Runner(script);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Set<Variable> parseVariables(final String scriptText) {
    final Set<String> variablesNames = Sets.newHashSet();
    final Matcher matcher = varPattern.matcher(scriptText);
    while(matcher.find()) {
      variablesNames.add(matcher.group());
    }

    final Set<Variable> variables = Sets.newHashSet();
    for (String name : variablesNames) {
      final String period = CharMatcher.digit().retainFrom(name);
      if (period.length() != 0) {
        variables.add(Variable.of(CharMatcher.digit().removeFrom(name)).withPeriod(Integer.parseInt(period)));
      } else {
        variables.add(Variable.of(name));
      }
    }
    return variables;
  }

  public Collection<Variable> getVariables() {
    return Sets.newHashSet(variables);
  }

  /**
   * Script Runner
   */
  public static class Runner implements ScriptRunner {
    private static final String ON_SIM_START_METHOD = "onSimulationStart";
    private static final String ON_SIM_END_METHOD = "onSimulationEnd";
    private static final String PROCESS_BAR_METHOD = "processBar";
    private static final String ON_BUY_FULFILLED_METHOD = "onBuyFulfilled";
    private static final String ON_SELL_FULFILLED_METHOD = "onSellFulfilled";

    private final Map<String, Object> varBindings = Maps.newHashMap();
    private final Binding binding = new Binding(varBindings);
    private final Script script;

    private Runner(final Script script) {
      this.script = script;
      this.script.setBinding(binding);
    }

    @Override
    public void onSimulationStart(final SimContext context) {
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

      script.invokeMethod(PROCESS_BAR_METHOD, null);
    }

    @Override
    public void onBuyFulfilled(BarData barData) {
      script.invokeMethod(ON_BUY_FULFILLED_METHOD, null);
    }

    @Override
    public void onSellFulfilled(BarData barData) {
      script.invokeMethod(ON_SELL_FULFILLED_METHOD, null);
    }

    @Override
    public void onSimulationEnd() {
      script.invokeMethod(ON_SIM_END_METHOD, null);
    }
  }
}
