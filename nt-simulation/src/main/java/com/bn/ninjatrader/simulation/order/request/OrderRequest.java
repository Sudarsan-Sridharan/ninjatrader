package com.bn.ninjatrader.simulation.order.request;

import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;

/**
 * @author bradwee2000@gmail.com
 */
public class OrderRequest {

    private final Broker broker;
    private final TransactionType txnType;

    public OrderRequest(final Broker broker,
                        final TransactionType txnType) {
        this.broker = broker;
        this.txnType = txnType;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void submit() {
        broker.submitOrder(this);
    }
}
