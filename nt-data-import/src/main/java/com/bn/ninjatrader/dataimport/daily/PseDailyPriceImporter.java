package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.thirdparty.pse.PseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class PseDailyPriceImporter extends AbstractDailyPriceImporter {
  private static final Logger LOG = LoggerFactory.getLogger(PseDailyPriceImporter.class);

  @Inject
  private PseService pseService;

  @Override
  protected List<DailyQuote> provideDailyQuotes(final LocalDate date) {
    return pseService.getAllDailyQuotes();
  }
}
