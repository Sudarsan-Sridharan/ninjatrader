package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
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
    @JsonSubTypes.Type(value = Constant.class, name = "const")
})
public interface Operation<T extends Data> {

  double getValue(final T t);

  @JsonIgnore
  Set<Variable> getVariables();
}
