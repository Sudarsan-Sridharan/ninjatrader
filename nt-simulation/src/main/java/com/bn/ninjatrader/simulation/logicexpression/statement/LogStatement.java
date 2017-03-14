package com.bn.ninjatrader.simulation.logicexpression.statement;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogStatement implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(LogStatement.class);

  public static final LogStatement message(final String msg, final Operation<BarData> ... params) {
    return new LogStatement(msg, Lists.newArrayList(params));
  }

  @JsonProperty("msg")
  private final String msg;

  @JsonProperty("params")
  private final List<Operation<BarData>> params;

  public LogStatement(@JsonProperty("msg") final String msg,
                      @JsonProperty("params") final List<Operation<BarData>> params) {
    this.msg = msg;
    this.params = params;
  }

  @Override
  public void run(final BarData barData) {
    final List<Double> paramValues = params.stream().map(param -> param.getValue(barData)).collect(Collectors.toList());
    LOG.info("{} {}", barData.getPrice().getDate(), String.format(msg, paramValues.toArray()));
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LogStatement that = (LogStatement) o;
    return Objects.equal(msg, that.msg) &&
        Objects.equal(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(msg, params);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("msg", msg)
        .add("params", params)
        .toString();
  }
}
