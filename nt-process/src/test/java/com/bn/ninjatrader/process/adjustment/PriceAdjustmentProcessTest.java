package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentProcessTest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentProcessTest.class);

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private PriceDao priceDao;
  private PriceAdjustmentCalculator calculator;

  private PriceAdjustmentProcess process;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    calculator = mock(PriceAdjustmentCalculator.class);

    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));

    process = new PriceAdjustmentProcess(priceDao, calculator);
  }

  @Test
  public void testRunWithSymbol_shouldCalculateForSymbol() {
    final ArgumentCaptor<SavePriceRequest> saveCaptor = ArgumentCaptor.forClass(SavePriceRequest.class);
    final Price price = mock(Price.class);
    final Price adjustedPrice = mock(Price.class);

    when(priceDao.findPrices().now()).thenReturn(Lists.newArrayList(price));
    when(calculator.calc(any(List.class), any(Operation.class))).thenReturn(Lists.newArrayList(adjustedPrice));

    process.process(PriceAdjustmentRequest.forSymbol("MEG").from(from).to(to).adjustment(Operations.startWith(2)));

    // Verify find all symbols not called
    verify(priceDao, times(0)).findAllSymbols();

    // Verify FindPriceRequest
    verify(priceDao.findPrices()).withSymbol("MEG");
    verify(priceDao.findPrices()).from(from);
    verify(priceDao.findPrices()).to(to);
    verify(priceDao.findPrices()).now();

    // Verify SaveRequest
    verify(priceDao).save(saveCaptor.capture());
    final SavePriceRequest saveRequest = saveCaptor.getValue();
    assertThat(saveRequest.getSymbol()).isEqualTo("MEG");
    assertThat(saveRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify Calculator called
    verify(calculator).calc(Lists.newArrayList(price), Operations.startWith(2));
  }
}
