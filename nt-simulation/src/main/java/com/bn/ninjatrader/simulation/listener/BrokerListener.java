package com.bn.ninjatrader.simulation.listener;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
public interface BrokerListener extends Serializable {

  /**
   * Called when a Buy order is successfully executed by Broker.
   * @param transaction The BuyTransaction containing details of the purchase
   * @param barData The bar data
   */
  void onFulfilledBuy(final BuyTransaction transaction, final BarData barData);

  /**
   * Called when a Sell order is successfully executed by Broker.
   * @param transaction The SellTransaction containing details of the sell
   * @param barData The bar data
   */
  void onFulfilledSell(final SellTransaction transaction, final BarData barData);

  /**
   * Called when a Cleanup order is successfully executed by Broker at the end of the simulation.
   * @param transaction The SellTransaction containing details of the sell
   * @param barData The bar data
   */
  void onCleanup(final SellTransaction transaction, final BarData barData);
}
