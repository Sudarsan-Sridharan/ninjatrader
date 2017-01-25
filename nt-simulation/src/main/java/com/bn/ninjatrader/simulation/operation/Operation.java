package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.function.HighestInNBarsFunction;
import com.bn.ninjatrader.simulation.operation.function.HistoryFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonSubTypes.Type(value = BinaryOperation.class, name = "op"),
    @JsonSubTypes.Type(value = Variable.class, name = "var"),
    @JsonSubTypes.Type(value = Constant.class, name = "const"),
    @JsonSubTypes.Type(value = HighestInNBarsFunction.class, name = "highest"),
    @JsonSubTypes.Type(value = HistoryFunction.class, name = "history")
})
public interface Operation {

  double getValue(final BarData barData);

  @JsonIgnore
  Set<Variable> getVariables();
}
