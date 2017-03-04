package com.bn.ninjatrader.simulation.logicexpression.operation;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/29/16.
 */
public class LowestValue implements Operation<BarData> {
  private static final Logger LOG = LoggerFactory.getLogger(LowestValue.class);
  private static final String TO_STRING_FORMAT = "lowestFrom%sTo%sBarsAgo(%s)";

  public static final LowestValue of(final Operation<BarData> operation) {
    return new LowestValue(Lists.newArrayList(operation), 0, 0);
  }

  public static final LowestValue of(final Operation<BarData> operation1,
                                     final Operation<BarData> operation2,
                                     final Operation<BarData> ... more) {
    return new LowestValue(Lists.asList(operation1, operation2, more), 0, 0);
  }

  @JsonProperty("operation")
  private final List<Operation<BarData>> operations = Lists.newArrayList();

  @JsonProperty("fromBarsAgo")
  private final int fromBarsAgo;

  @JsonProperty("toBarsAgo")
  private final int toBarsAgo;

  public LowestValue(@JsonProperty("operation") final List<Operation<BarData>> operations,
                     @JsonProperty("numOfBarsAgo") final int fromBarsAgo,
                     @JsonProperty("toBarsAgo") final int toBarsAgo) {
    checkNotNull(operations, "operations must not be null.");
    checkArgument(operations.size() > 0, "must have at least 1 operation.");
    checkArgument(fromBarsAgo >= 0, "fromBarsAgo must be >= 0.");
    checkArgument(toBarsAgo >= 0, "toBarsAgo must be >= 0.");

    this.operations.addAll(operations);
    this.fromBarsAgo = fromBarsAgo;
    this.toBarsAgo = toBarsAgo;
  }

  public LowestValue fromBarsAgo(final int fromBarsAgo) {
    return new LowestValue(operations, fromBarsAgo, toBarsAgo);
  }

  public LowestValue toBarsAgo(final int toBarsAgo) {
    return new LowestValue(operations, fromBarsAgo, toBarsAgo);
  }

  @Override
  public double getValue(final BarData barData) {
    final History history = barData.getWorld().getHistory();
    double lowest = Double.MAX_VALUE;

    int from = Math.min(fromBarsAgo, toBarsAgo);
    int to = Math.max(fromBarsAgo, toBarsAgo);

    for (int i=from; i <= to; i++) {
      final Optional<BarData> pastBarData = history.getNBarsAgo(i);
      if (pastBarData.isPresent()) {
        lowest = Math.min(lowest, lowestValueAmongOperations(pastBarData.get()));
      }
    }
    return lowest;
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> set = Sets.newHashSet();
    for (final Operation operation : operations) {
      set.addAll(operation.getVariables());
    }
    return set;
  }

  private double lowestValueAmongOperations(final BarData barData) {
    double lowest = Double.MAX_VALUE;
    for (final Operation operation : operations) {
      lowest = Math.min(lowest, operation.getValue(barData));
    }
    return lowest;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof LowestValue)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final LowestValue rhs = (LowestValue) obj;
    return Objects.equal(operations, rhs.operations)
        && Objects.equal(fromBarsAgo, rhs.fromBarsAgo)
        && Objects.equal(toBarsAgo, rhs.toBarsAgo);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(operations, fromBarsAgo, toBarsAgo);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("operations", operations).add("fromBarsAgo", fromBarsAgo).add("toBarsAgo", toBarsAgo)
        .toString();
  }

  @Override
  public String toString(final BarData barData) {
    return String.format(TO_STRING_FORMAT, fromBarsAgo, toBarsAgo, NumUtil.trim(getValue(barData), 4));
  }
}
