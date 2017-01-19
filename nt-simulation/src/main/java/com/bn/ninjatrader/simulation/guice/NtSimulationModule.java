package com.bn.ninjatrader.simulation.guice;

import com.bn.ninjatrader.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.simulation.data.provider.*;
import com.bn.ninjatrader.simulation.guice.annotation.AllDataProviders;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.util.List;

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
}
