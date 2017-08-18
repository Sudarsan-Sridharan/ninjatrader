package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.common.model.Price;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentServiceTest.class);

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private PriceDao priceDao;

  private PriceAdjustmentService service;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);

    when(priceDao.savePrices(any())).thenReturn(mock(PriceDao.SavePricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));

    service = new PriceAdjustmentService(priceDao);
  }

  @Test
  public void testRunWithSymbol_shouldAdjustPricesForSymbol() {
    final Price price = Price.builder().date(from)
        .open(10).high(20).low(7).close(15).volume(1000).build();

    when(priceDao.findPrices().now()).thenReturn(Lists.newArrayList(price));

    final List<Price> adjustedPrices = service.preparePriceAdjustment()
        .symbol("MEG").from(from).to(to).script("return $PRICE / 2").execute();

    // Verify FindPriceRequest
    verify(priceDao.findPrices()).withSymbol("MEG");
    verify(priceDao.findPrices()).from(from);
    verify(priceDao.findPrices()).to(to);
    verify(priceDao.findPrices()).now();

    // Verify Save
    verify(priceDao).savePrices(adjustedPrices);
    verify(priceDao.savePrices(any())).withSymbol("MEG");
    verify(priceDao.savePrices(any())).withTimeFrame(TimeFrame.ONE_DAY);

    final Price expectedPrice = Price.builder().date(from)
        .open(5).high(10).low(3.5).close(7.5).volume(1000).build();

    assertThat(adjustedPrices).containsExactly(expectedPrice);
  }
}
