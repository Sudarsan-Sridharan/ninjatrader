package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.thirdparty.investagrams.InvestagramsService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class InvestagramsDailyPriceImporter extends AbstractDailyPriceImporter{

  private static final Logger LOG = LoggerFactory.getLogger(InvestagramsDailyPriceImporter.class);

  @Inject
  private InvestagramsService investagramsService;

  @Override
  protected List<DailyQuote> provideDailyQuotes() {
    return investagramsService.getDailyQuotes();
  }

  public static void main(String args[]) throws IOException {
    Injector injector = Guice.createInjector(
        new NtModelModule()
    );

    InvestagramsDailyPriceImporter app = injector.getInstance(InvestagramsDailyPriceImporter.class);
    app.importData();
  }
}
