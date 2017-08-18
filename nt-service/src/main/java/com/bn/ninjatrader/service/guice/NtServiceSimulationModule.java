package com.bn.ninjatrader.service.guice;

import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.scanner.DefaultStockScanner;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceSimulationModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new NtSimulationModule());

    bind(StockScanner.class).to(DefaultStockScanner.class);
  }
}
