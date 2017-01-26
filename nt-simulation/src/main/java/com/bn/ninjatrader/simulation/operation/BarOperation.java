package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.function.HighestFunction;
import com.bn.ninjatrader.simulation.operation.function.HistoryFunction;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author bradwee2000@gmail.com
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HistoryFunction.class, name = "history"),
    @JsonSubTypes.Type(value = HighestFunction.class, name = "highest")
})
public interface BarOperation extends Operation<BarData> {
}
