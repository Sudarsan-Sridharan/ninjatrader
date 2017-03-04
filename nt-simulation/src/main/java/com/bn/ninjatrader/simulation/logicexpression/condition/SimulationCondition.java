package com.bn.ninjatrader.simulation.logicexpression.condition;

import com.bn.ninjatrader.logical.expression.condition.Condition;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author bradwee2000@gmail.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_t")
@JsonSubTypes({
    @JsonSubTypes.Type(value=PortfolioHasShares.class, name="portHasShares")
})
public interface SimulationCondition extends Condition<BarData> {
}
