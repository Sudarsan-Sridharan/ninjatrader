package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    property = "_class"
)
public interface Operation<T extends Data> {

  double getValue(final T t);

  @JsonIgnore
  Set<Variable> getVariables();
}
