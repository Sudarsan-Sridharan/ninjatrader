package com.bn.ninjatrader.simulation.order.request;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @author bradwee2000@gmail.com
 */
public class SellOrderRequest extends OrderRequest {

    private String symbol;
    private OrderType orderType = OrderTypes.marketClose();
    private OrderConfig orderConfig = OrderConfig.defaults();

    @Inject
    public SellOrderRequest(final BoardLotTable boardLotTable,
                            @Assisted final Broker broker) {
        super(boardLotTable, broker, TransactionType.SELL);
    }

    public SellOrderRequest withSymbol(final String symbol) {
        this.symbol = symbol;
        return this;
    }

    public SellOrderRequest withBarsFromNow(final int barsFromNow) {
        this.orderConfig = orderConfig.barsFromNow(barsFromNow);
        return this;
    }

    public SellOrderRequest withExpireAfterNumOfBars(final int expiry) {
        this.orderConfig = orderConfig.expireAfterNumOfBars(expiry);
        return this;
    }

    public SellOrderRequest atPrice(final double price) {
        this.orderType = OrderTypes.atPrice(roundPrice(price));
        return this;
    }

    public SellOrderRequest atClose() {
        this.orderType = OrderTypes.marketClose();
        return this;
    }

    public SellOrderRequest atOpen() {
        this.orderType = OrderTypes.marketOpen();
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderConfig getOrderConfig() {
        return orderConfig;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("symbol", symbol)
            .add("orderType", orderType)
            .add("orderConfig", orderConfig)
            .toString();
    }
}
