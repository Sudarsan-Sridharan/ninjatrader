package com.bn.ninjatrader.simulation.listener;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;

/**
 * @author bradwee2000@gmail.com
 */
public interface BrokerListener {
  void onFulfilledBuy(final BuyTransaction transaction, final BarData barData);

  void onFulfilledSell(final SellTransaction transaction, final BarData barData);
}
