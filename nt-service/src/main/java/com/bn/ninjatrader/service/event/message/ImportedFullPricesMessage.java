package com.bn.ninjatrader.service.event.message;

import com.bn.ninjatrader.event.message.Message;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import static com.bn.ninjatrader.service.event.EventTypes.IMPORTED_FULL_PRICES;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportedFullPricesMessage extends Message<List<DailyQuote>> {

  public ImportedFullPricesMessage(final Collection<DailyQuote> quotes) {
    super(IMPORTED_FULL_PRICES, ImmutableList.copyOf(quotes));
    checkNotNull(quotes);
  }
}
