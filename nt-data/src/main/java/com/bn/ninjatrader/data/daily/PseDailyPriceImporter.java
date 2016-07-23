package com.bn.ninjatrader.data.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.thirdparty.pse.PseService;
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
public class PseDailyPriceImporter extends AbstractDailyPriceImporter {

  private static final Logger log = LoggerFactory.getLogger(PseDailyPriceImporter.class);

  @Inject
  private PseService pseService;

  @Override
  protected List<DailyQuote> provideDailyQuotes() {
    return pseService.getDailyQuotes();
  }

  public static void main(String args[]) throws IOException {
    Injector injector = Guice.createInjector(
        new NtModelModule()
    );

    PseDailyPriceImporter app = injector.getInstance(PseDailyPriceImporter.class);
    app.importData();
  }
}
