package com.bn.ninjatrader.simulation.order.processor;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.request.OrderRequest;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
public interface OrderRequestProcessor extends Serializable {

    Order process(final OrderRequest req, final BarData barData);
}
