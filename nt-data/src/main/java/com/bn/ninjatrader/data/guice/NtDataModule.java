package com.bn.ninjatrader.data.guice;

import com.bn.ninjatrader.data.history.parser.CsvDataParser;
import com.bn.ninjatrader.data.history.parser.DataParser;
import com.google.inject.AbstractModule;

/**
 * Created by Brad on 4/28/16.
 */
public class NtDataModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(DataParser.class).to(CsvDataParser.class);
  }
}
