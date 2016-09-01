package com.bn.ninjatrader.testplay.simulation.guice;

import com.bn.ninjatrader.testplay.simulation.datafinder.*;
import com.bn.ninjatrader.testplay.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.testplay.simulation.guice.annotation.AllDataFinders;
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
  @AllDataFinders
  private List<DataFinder> provideAllDataFinders(
      PriceDataFinder priceDataFinder,
      IchimokuDataFinder ichimokuDataFinder,
      SMADataFinder smaDataFinder,
      RSIDataFinder rsiDataFinder) {

    List<DataFinder> dataFinders = Lists.newArrayList(
        priceDataFinder,
        ichimokuDataFinder,
        smaDataFinder,
        rsiDataFinder);
    return dataFinders;
  }
}
