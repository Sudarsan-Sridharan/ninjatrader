package com.bn.ninjatrader.simulation.logicexpression.statement;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_t"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CancelOrderStatement.class, name = "cancel"),
    @JsonSubTypes.Type(value = MultiStatement.class, name = "multi"),

})
public interface Statement {

  void run(final BarData barData);

  @JsonIgnore
  Set<Variable> getVariables();
}
