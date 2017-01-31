package com.bn.ninjatrader.simulation.order.cancel;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * CancelOption
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CancelAll.class, name = "all"),
})
public interface CancelType {
  List<PendingOrder> findPendingOrdersToCancel(final BarData barData);
}
