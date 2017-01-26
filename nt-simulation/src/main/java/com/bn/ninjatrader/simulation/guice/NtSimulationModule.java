package com.bn.ninjatrader.simulation.guice;

import com.bn.ninjatrader.simulation.model.BrokerFactory;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.data.provider.*;
import com.bn.ninjatrader.simulation.annotation.AllDataProviders;
import com.bn.ninjatrader.simulation.annotation.OrderExecutors;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.util.List;
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
  @AllDataProviders
  private List<DataProvider> provideAllDataProviders(
      final PriceDataProvider priceDataProvider,
      final IchimokuDataProvider ichimokuDataProvider,
      final EMADataProvider emaDataProvider,
      final SMADataProvider smaDataProvider,
      final RSIDataProvider rsiDataProvider) {

    final List<DataProvider> dataProviders = Lists.newArrayList(
        priceDataProvider,
        ichimokuDataProvider,
        emaDataProvider,
        smaDataProvider,
        rsiDataProvider);
    return dataProviders;
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
}
