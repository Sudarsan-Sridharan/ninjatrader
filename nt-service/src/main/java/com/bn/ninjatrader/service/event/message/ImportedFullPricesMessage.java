package com.bn.ninjatrader.service.event.message;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.messaging.Message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.service.event.EventTypes.IMPORTED_FULL_PRICES;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportedFullPricesMessage extends Message<List<DailyQuote>> implements Serializable {

  /**
   * Needed for json.
   */
  public ImportedFullPricesMessage() {
    super(IMPORTED_FULL_PRICES, Collections.emptyList());
  }

  public ImportedFullPricesMessage(final Collection<DailyQuote> quotes) {
    super(IMPORTED_FULL_PRICES, ImmutableList.copyOf(quotes));
    checkNotNull(quotes);
  }
}
