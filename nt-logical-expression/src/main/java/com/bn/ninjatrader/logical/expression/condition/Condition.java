package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
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
    @JsonSubTypes.Type(value = AndCondition.class, name = "and"),
    @JsonSubTypes.Type(value = OrCondition.class, name = "or"),
    @JsonSubTypes.Type(value = BasicCondition.class, name = "basic")
})
public interface Condition<T extends Data> {

  boolean isMatch(final T t);

  @JsonIgnore
  Set<Variable> getVariables();
}
