package com.bn.ninjatrader.simulation.exception;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class VariableUnknownException extends RuntimeException {
  private static final String ERROR_MSG = "Variable [%s] is unknown.";
  private final Variable variable;

  public VariableUnknownException(final Variable variable) {
    super(String.format(ERROR_MSG, variable.getName()));
    this.variable = variable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VariableUnknownException that = (VariableUnknownException) o;
    return Objects.equal(variable, that.variable);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(variable);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("variable", variable)
        .toString();
  }
}
