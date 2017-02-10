package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiStatement implements Statement {

  public static final Builder builder() {
    return new Builder();
  }

  public static final MultiStatement of(final Statement statement) {
    return new MultiStatement(Lists.newArrayList(statement));
  }

  public static final MultiStatement of(final Statement statement1,
                                        final Statement statement2,
                                        final Statement ... more) {
    return new MultiStatement(Lists.asList(statement1, statement2, more));
  }

  @JsonProperty("statements")
  private final List<Statement> statementList = Lists.newArrayList();

  public MultiStatement(@JsonProperty("statements") final List<Statement> statementList) {
    this.statementList.addAll(statementList);
  }

  @Override
  public void run(final BarData barData) {
    for (final Statement statement : statementList) {
      statement.run(barData);
    }
  }

  public List<Statement> getStatementList() {
    return Lists.newArrayList(statementList);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> set = Sets.newHashSet();
    for (final Statement statement : statementList) {
      set.addAll(statement.getVariables());
    }
    return set;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof MultiStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final MultiStatement rhs = (MultiStatement) obj;
    return Objects.equal(statementList, rhs.statementList);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(statementList);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("statements", statementList).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private final List<Statement> statementList = Lists.newArrayList();

    public Builder add(final Statement statement) {
      statementList.add(statement);
      return this;
    }

    public Builder addAll(final Statement statement, final Statement ... more) {
      statementList.addAll(Lists.asList(statement, more));
      return this;
    }

    public Builder addAll(final Collection<Statement> statements) {
      if (statements != null) {
        statementList.addAll(statements);
      }
      return this;
    }

    public MultiStatement build() {
      return new MultiStatement(statementList);
    }
  }
}
