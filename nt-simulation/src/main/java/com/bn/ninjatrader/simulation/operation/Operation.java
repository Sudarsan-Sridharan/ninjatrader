package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnaryOperation.class, name = "UNARY"),
    @JsonSubTypes.Type(value = BinaryOperation.class, name = "BINARY")
})
public interface Operation {
  double getValue(BarData barData);

  Set<Variable> getVariables();

  OperationType getOperationType();
}
