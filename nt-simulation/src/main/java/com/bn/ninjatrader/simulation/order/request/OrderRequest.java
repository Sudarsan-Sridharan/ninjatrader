package com.bn.ninjatrader.simulation.order.request;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.transaction.TransactionType;

/**
 * @author bradwee2000@gmail.com
 */
public class OrderRequest {

    private final Broker broker;
    private final TransactionType txnType;
    private final BoardLotTable boardLotTable;

    public OrderRequest(final BoardLotTable boardLotTable,
                        final Broker broker,
                        final TransactionType txnType) {
        this.boardLotTable = boardLotTable;
        this.broker = broker;
        this.txnType = txnType;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public double roundPrice(final double price) {
        final int decimalPlaces = boardLotTable.getBoardLot(price).getDecimalPlaces();
        return NumUtil.round(price, decimalPlaces);
    }

    public void submit() {
        broker.submitOrder(this);
    }
}
