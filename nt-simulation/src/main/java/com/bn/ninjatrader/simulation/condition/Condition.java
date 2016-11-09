package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Variable;
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
    @JsonSubTypes.Type(value = AndCondition.class, name = "AND"),
    @JsonSubTypes.Type(value = OrCondition.class, name = "OR"),
    @JsonSubTypes.Type(value = BasicCondition.class, name = "BASIC")
})
public interface Condition {

  boolean isMatch(BarData barParameters);

  Set<Variable> getVariables();

  ConditionType getConditionType();
}
