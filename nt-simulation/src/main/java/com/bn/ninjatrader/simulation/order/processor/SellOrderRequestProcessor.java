package com.bn.ninjatrader.simulation.order.processor;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.order.request.OrderRequest;
import com.bn.ninjatrader.simulation.order.request.SellOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SellOrderRequestProcessor implements OrderRequestProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(SellOrderRequestProcessor.class);
    private static final String NULL_ORDER = "Order must not be null.";
    private static final String INVALID_ORDER_TYPE = "Order is not a SELL order request.";

    @Override
    public Order process(final OrderRequest req, final BarData barData) {
        checkNotNull(req, NULL_ORDER);
        checkArgument(req instanceof SellOrderRequest, INVALID_ORDER_TYPE);

        final SellOrderRequest sellReq = (SellOrderRequest) req;
        final String symbol = sellReq.getSymbol();

        final Portfolio portfolio = barData.getSimulationContext().getAccount().getPortfolio();
        final long totalSharesToSell = portfolio.getTotalShares(symbol);

        if (portfolio.canCommitShares(symbol, totalSharesToSell)) {
            portfolio.commitShares(symbol, totalSharesToSell);
        }

        final Order order = SellOrder.builder()
            .symbol(sellReq.getSymbol())
            .config(sellReq.getOrderConfig())
            .type(sellReq.getOrderType())
            .date(barData.getPrice().getDate())
            .build();
        return order;
    }
}
