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
    @JsonSubTypes.Type(value = BuyOrderStatement.class, name = "buy"),
    @JsonSubTypes.Type(value = SellOrderStatement.class, name = "sell"),
    @JsonSubTypes.Type(value = CancelOrderStatement.class, name = "cancel"),
    @JsonSubTypes.Type(value = ConditionalStatement.class, name = "cond"),
    @JsonSubTypes.Type(value = SetPropertyStatement.class, name = "setProp"),
    @JsonSubTypes.Type(value = MarkStatement.class, name = "mark"),
    @JsonSubTypes.Type(value = MultiStatement.class, name = "multi"),
    @JsonSubTypes.Type(value = EmptyStatement.class, name = "empty"),
    @JsonSubTypes.Type(value = LogStatement.class, name = "log"),
})
public interface Statement {

  void run(final BarData barData);

  @JsonIgnore
  Set<Variable> getVariables();
}
