package com.bn.ninjatrader.simulation.guice;

import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.annotation.VarCalculatorMap;
import com.bn.ninjatrader.simulation.calculator.BarIndexVarCalculator;
import com.bn.ninjatrader.simulation.calculator.EmaVarCalculator;
import com.bn.ninjatrader.simulation.calculator.PriceVarCalculator;
import com.bn.ninjatrader.simulation.calculator.SmaVarCalculator;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.model.BrokerFactory;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.util.Map;

/**
 * Created by Brad on 8/20/16.
 */
public class NtSimulationModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(BrokerFactory.class));
  }

  @Provides
  @OrderExecutors
  private Map<TransactionType, OrderExecutor> provideOrderExecutors(final BuyOrderExecutor buyOrderExecutor,
                                                                    final SellOrderExecutor sellOrderExecutor) {
    final Map<TransactionType, OrderExecutor> orderExecutors = Maps.newHashMap();
    orderExecutors.put(TransactionType.BUY, buyOrderExecutor);
    orderExecutors.put(TransactionType.SELL, sellOrderExecutor);
    return orderExecutors;
  }

  @Provides
  @VarCalculatorMap
  private Map<String, Class> provideVarCalculators() {
    final Map<String, Class> map = Maps.newHashMap();
    map.put(DataType.BAR_INDEX, BarIndexVarCalculator.class);
    map.put(DataType.PRICE_OPEN, PriceVarCalculator.class);
    map.put(DataType.PRICE_HIGH, PriceVarCalculator.class);
    map.put(DataType.PRICE_LOW, PriceVarCalculator.class);
    map.put(DataType.PRICE_CLOSE, PriceVarCalculator.class);
    map.put(DataType.VOLUME, PriceVarCalculator.class);
    map.put(DataType.SMA, SmaVarCalculator.class);
    map.put(DataType.EMA, EmaVarCalculator.class);
    return map;
  }
}
