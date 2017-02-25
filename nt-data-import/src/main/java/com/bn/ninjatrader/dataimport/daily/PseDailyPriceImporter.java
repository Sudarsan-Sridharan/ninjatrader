package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.dao.PriceDao;
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

  private final PseService pseService;

  @Inject
  public PseDailyPriceImporter(final PseService pseService,
                               final PriceDao priceDao,
                               final PriceBuilderFactory priceBuilderFactory) {
    super(priceDao, priceBuilderFactory);
    this.pseService = pseService;
  }

  @Override
  protected List<DailyQuote> provideDailyQuotes(final LocalDate date) {
    return pseService.getAllDailyQuotes();
  }
}
