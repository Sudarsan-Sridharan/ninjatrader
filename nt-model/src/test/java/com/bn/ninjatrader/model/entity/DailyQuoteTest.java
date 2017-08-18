package com.bn.ninjatrader.model.entity;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class DailyQuoteTest {
  private static final Logger LOG = LoggerFactory.getLogger(DailyQuoteTest.class);

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final DailyQuote orig = new DailyQuote("MEG", now, 1, 2, 3, 4, 1000);
  private final DailyQuote equal = new DailyQuote("MEG", now, 1, 2, 3, 4, 1000);
  private final DailyQuote diffSym = new DailyQuote("BDO", now, 1, 2, 3, 4, 1000);
  private final DailyQuote diffDate = new DailyQuote("MEG", now.plusDays(1), 1, 2, 3, 4, 1000);
  private final DailyQuote diffOpen = new DailyQuote("MEG", now, 10, 2, 3, 4, 1000);
  private final DailyQuote diffHigh = new DailyQuote("MEG", now, 1, 20, 3, 4, 1000);
  private final DailyQuote diffLow = new DailyQuote("MEG", now, 1, 2, 30, 4, 1000);
  private final DailyQuote diffClose = new DailyQuote("MEG", now, 1, 2, 3, 40, 1000);
  private final DailyQuote diffVol = new DailyQuote("MEG", now, 1, 2, 3, 4, 99000);

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffSym)
        .isNotEqualTo(diffDate)
        .isNotEqualTo(diffOpen)
        .isNotEqualTo(diffHigh)
        .isNotEqualTo(diffLow)
        .isNotEqualTo(diffClose)
        .isNotEqualTo(diffVol);
  }

  @Test
  public void testHashCode_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffSym, diffDate, diffOpen, diffHigh, diffLow, diffClose, diffVol))
        .containsExactlyInAnyOrder(orig, diffSym, diffDate, diffOpen, diffHigh, diffLow, diffClose, diffVol);
  }
}
