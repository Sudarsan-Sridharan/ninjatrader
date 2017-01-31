package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BuyOrderStatement.class, name = "buy"),
    @JsonSubTypes.Type(value = SellOrderStatement.class, name = "sell"),
    @JsonSubTypes.Type(value = CancelOrderStatement.class, name = "cancel"),
    @JsonSubTypes.Type(value = ConditionalStatement.class, name = "cond"),
    @JsonSubTypes.Type(value = SetPropertyStatement.class, name = "prop"),
    @JsonSubTypes.Type(value = MultiStatement.class, name = "multi"),
    @JsonSubTypes.Type(value = EmptyStatement.class, name = "empty")
})
public interface Statement {

  void run(final BarData barData);

  Set<Variable> getVariables();
}
