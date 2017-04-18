package com.bn.ninjatrader.simulation.order.processor;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.request.BuyOrderRequest;
import com.bn.ninjatrader.simulation.order.request.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class BuyOrderRequestProcessor implements OrderRequestProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BuyOrderRequestProcessor.class);
    private static final String NULL_ORDER = "Order must not be null.";
    private static final String INVALID_ORDER_TYPE = "Order is not a BUY order request.";

    @Override
    public Order process(final OrderRequest req, final BarData barData) {
        checkNotNull(req, NULL_ORDER);
        checkArgument(req instanceof BuyOrderRequest, INVALID_ORDER_TYPE);

        final BuyOrderRequest buyReq = (BuyOrderRequest) req;
        final Order order = BuyOrder.builder()
            .symbol(buyReq.getSymbol())
            .cashAmount(buyReq.getCashAmount())
            .config(buyReq.getOrderConfig())
            .type(buyReq.getOrderType())
            .date(barData.getPrice().getDate())
            .build();

        return order;
    }
}
