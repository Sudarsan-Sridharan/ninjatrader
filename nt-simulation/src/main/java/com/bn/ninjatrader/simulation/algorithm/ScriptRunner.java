package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.transaction.*;

/**
 * @author bradwee2000@gmail.com
 */
public interface ScriptRunner {

  void onSimulationStart(final SimulationContext context);

  void processBar(final BarData barData);

  void onBuyFulfilled(BuyTransaction transaction, final BarData barData);

  void onSellFulfilled(SellTransaction transaction, final BarData barData);

  void onSimulationEnd();
}
