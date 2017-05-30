package com.bn.ninjatrader.simulation.order.request;

import com.bn.ninjatrader.simulation.model.Broker;

/**
 * @author bradwee2000@gmail.com
 */
public interface OrderRequestFactory {

    BuyOrderRequest createBuyOrderRequest(final Broker broker);

    SellOrderRequest createSellOrderRequest(final Broker broker);
}
