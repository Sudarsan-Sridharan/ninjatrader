package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.bn.ninjatrader.simulation.exception.ScriptCompileErrorException;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
public class GroovyAlgorithmScript implements AlgorithmScript {
  private static final Logger LOG = LoggerFactory.getLogger(GroovyAlgorithmScript.class);
  private static final String BARS_AGO_REGEX = "\\[\\d+\\]";
  private static final String BLANK = "";
  private static final Pattern varPattern = Pattern.compile("\\$[\\w\\[\\]]+");
  private static final Pattern barsAgoPattern = Pattern.compile(BARS_AGO_REGEX);

  private static final GroovyShell shell = new GroovyShell();

  private final Set<ScriptVariable> scriptVariables;
  private final Set<ScriptVariable> historicalVariables;
  private final Set<Variable> variables;
  private final Class scriptClass;

  public GroovyAlgorithmScript(final String scriptText) {
    scriptVariables = Collections.unmodifiableSet(parseScriptVariables(scriptText));
    variables = scriptVariables.stream().map(v -> v.getVariable()).collect(Collectors.toSet());
    historicalVariables = scriptVariables.stream().filter(v -> v.getBarsAgo() > 0).collect(Collectors.toSet());

    String convertedSafeScript = scriptText;
    for (final ScriptVariable v : historicalVariables) {
      LOG.info("REPLACING: {} with {}", v.getName(), v.getSafeName());
      convertedSafeScript = convertedSafeScript.replace(v.getName(), v.getSafeName());
    }

    LOG.info("SAFE SCRIPT: {}", convertedSafeScript);

    try {
      // Cache the class so we don't keep recompiling script.
      scriptClass = shell.getClassLoader().parseClass(convertedSafeScript);
    } catch (final MultipleCompilationErrorsException e) {
      throw new ScriptCompileErrorException(e);
    }
  }

  @Override
  public ScriptRunner newRunner() {
    try {
      final Script script = (Script) scriptClass.newInstance();
      return new GroovyScriptRunner(script, historicalVariables);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Set<ScriptVariable> parseScriptVariables(final String scriptText) {
    final Set<String> variablesNames = parseVariableNames(scriptText);

    final Set<ScriptVariable> variables = Sets.newHashSet();
    for (final String name : variablesNames) {
      final Matcher barsAgoMatcher = barsAgoPattern.matcher(name);

      // Extract bars ago
      int barsAgo = 0;
      if (barsAgoMatcher.find()) {
        final String barsAgoGroup = barsAgoMatcher.group();
        barsAgo = Integer.parseInt(CharMatcher.digit().retainFrom(barsAgoGroup));
      }

      // Historical part removed
      final String filtered = name.replaceAll(BARS_AGO_REGEX, BLANK);

      // Extract period
      final String period = CharMatcher.digit().retainFrom(filtered);

      final Variable basicVariable = period.length() == 0 ?
            Variable.of(filtered) :
            Variable.of(CharMatcher.digit().removeFrom(filtered)).withPeriod(Integer.parseInt(period));

      variables.add(ScriptVariable.of(basicVariable).withBarsAgo(barsAgo));
    }
    return variables;
  }

  private Set<String> parseVariableNames(final String scriptText) {
    final Set<String> variablesNames = Sets.newHashSet();
    final Matcher matcher = varPattern.matcher(scriptText);
    while(matcher.find()) {
      variablesNames.add(matcher.group());
    }
    return variablesNames;
  }

  public Collection<Variable> getVariables() {
    return Sets.newHashSet(variables);
  }

  public Set<ScriptVariable> getScriptVariables() {
    return scriptVariables;
  }
}
