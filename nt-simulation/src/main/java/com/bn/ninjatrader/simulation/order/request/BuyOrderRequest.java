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
public class BuyOrderRequest extends OrderRequest {

    private String symbol;
    private OrderType orderType = OrderTypes.marketClose();
    private OrderConfig orderConfig = OrderConfig.defaults();
    private double cashAmount;

    public BuyOrderRequest(final Broker broker) {
        super(broker, TransactionType.BUY);
    }

    public BuyOrderRequest withCashAmount(final double cashAmount) {
        this.cashAmount = cashAmount;
        return this;
    }

    public BuyOrderRequest withSymbol(final String symbol) {
        this.symbol = symbol;
        return this;
    }

    public BuyOrderRequest withBarsFromNow(final int barsFromNow) {
        this.orderConfig = orderConfig.barsFromNow(barsFromNow);
        return this;
    }

    public BuyOrderRequest withExpireAfterNumOfBars(final int expiry) {
        this.orderConfig = orderConfig.expireAfterNumOfBars(expiry);
        return this;
    }

    public BuyOrderRequest atPrice(final double price) {
        this.orderType = OrderTypes.atPrice(price);
        return this;
    }

    public BuyOrderRequest atClose() {
        this.orderType = OrderTypes.marketClose();
        return this;
    }

    public BuyOrderRequest atOpen() {
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

    public double getCashAmount() {
        return cashAmount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("symbol", symbol)
            .add("orderType", orderType)
            .add("orderConfig", orderConfig)
            .add("cashAmount", cashAmount)
            .toString();
    }
}
