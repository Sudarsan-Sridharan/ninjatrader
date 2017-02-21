package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.thirdparty.investagrams.InvestagramsService;
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
public class InvestagramsDailyPriceImporter extends AbstractDailyPriceImporter{

  private static final Logger LOG = LoggerFactory.getLogger(InvestagramsDailyPriceImporter.class);

  private final InvestagramsService investagramsService;

  @Inject
  public InvestagramsDailyPriceImporter(final InvestagramsService investagramsService,
                                        final PriceDao priceDao) {
    super(priceDao);
    this.investagramsService = investagramsService;
  }

  @Override
  protected List<DailyQuote> provideDailyQuotes(final LocalDate date) {
    return investagramsService.getDailyQuotes();
  }
}
