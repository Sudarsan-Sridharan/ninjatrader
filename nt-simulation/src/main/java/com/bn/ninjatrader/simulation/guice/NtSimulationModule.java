package com.bn.ninjatrader.simulation.guice;

import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.annotation.OrderRequestProcessors;
import com.bn.ninjatrader.simulation.annotation.VarCalculatorMap;
import com.bn.ninjatrader.simulation.binding.BarIndexBindingProvider;
import com.bn.ninjatrader.simulation.binding.EmaBindingProvider;
import com.bn.ninjatrader.simulation.binding.PriceBindingProvider;
import com.bn.ninjatrader.simulation.binding.SmaBindingProvider;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.model.BrokerFactory;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.order.processor.BuyOrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.processor.OrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.processor.SellOrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.request.OrderRequestFactory;
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
    install(new FactoryModuleBuilder().build(OrderRequestFactory.class));
  }

  @Provides
  @OrderRequestProcessors
  private Map<TransactionType, OrderRequestProcessor> provideOrderRequestProcessors(
      final BuyOrderRequestProcessor buyOrderRequestProcessor,
      final SellOrderRequestProcessor sellOrderRequestProcessor) {
    final Map<TransactionType, OrderRequestProcessor> orderProcessors = Maps.newHashMap();
    orderProcessors.put(TransactionType.BUY, buyOrderRequestProcessor);
    orderProcessors.put(TransactionType.SELL, sellOrderRequestProcessor);
    return orderProcessors;
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
    map.put(DataType.BAR_INDEX, BarIndexBindingProvider.class);
    map.put(DataType.PRICE_OPEN, PriceBindingProvider.class);
    map.put(DataType.PRICE_HIGH, PriceBindingProvider.class);
    map.put(DataType.PRICE_LOW, PriceBindingProvider.class);
    map.put(DataType.PRICE_CLOSE, PriceBindingProvider.class);
    map.put(DataType.DATE, PriceBindingProvider.class);
    map.put(DataType.VOLUME, PriceBindingProvider.class);
    map.put(DataType.SMA, SmaBindingProvider.class);
    map.put(DataType.EMA, EmaBindingProvider.class);
    return map;
  }
}
