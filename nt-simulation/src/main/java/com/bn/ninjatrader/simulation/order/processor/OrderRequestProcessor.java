package com.bn.ninjatrader.simulation.order.processor;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.request.OrderRequest;

/**
 * @author bradwee2000@gmail.com
 */
public interface OrderRequestProcessor {

    Order process(final OrderRequest req, final BarData barData);
}
