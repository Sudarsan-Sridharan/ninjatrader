package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentProcessTest {

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private PriceDao priceDao;
  private PriceAdjustmentCalculator calculator;

  private PriceAdjustmentProcess process;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    calculator = mock(PriceAdjustmentCalculator.class);

    process = new PriceAdjustmentProcess(priceDao, calculator);
  }

  @Test
  public void testRunWithSymbol_shouldCalculateForSymbol() {
    final ArgumentCaptor<FindPriceRequest> findCaptor = ArgumentCaptor.forClass(FindPriceRequest.class);
    final ArgumentCaptor<SavePriceRequest> saveCaptor = ArgumentCaptor.forClass(SavePriceRequest.class);
    final Price price = mock(Price.class);
    final Price adjustedPrice = mock(Price.class);

    when(priceDao.find(any(FindPriceRequest.class))).thenReturn(Lists.newArrayList(price));
    when(calculator.calc(any(List.class), any(Operation.class))).thenReturn(Lists.newArrayList(adjustedPrice));

    process.process(PriceAdjustmentRequest.forSymbol("MEG").from(from).to(to).adjustment(Operations.startWith(2)));

    // Verify find all symbols not called
    verify(priceDao, times(0)).findAllSymbols();

    // Verify FindPriceRequest
    verify(priceDao).find(findCaptor.capture());
    final FindPriceRequest FindPriceRequest = findCaptor.getValue();
    assertThat(FindPriceRequest.getSymbol()).isEqualTo("MEG");
    assertThat(FindPriceRequest.getFromDate()).isEqualTo(from);
    assertThat(FindPriceRequest.getToDate()).isEqualTo(to);
    assertThat(FindPriceRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify SaveRequest
    verify(priceDao).save(saveCaptor.capture());
    final SavePriceRequest saveRequest = saveCaptor.getValue();
    assertThat(saveRequest.getSymbol()).isEqualTo("MEG");
    assertThat(saveRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify Calculator called
    verify(calculator).calc(Lists.newArrayList(price), Operations.startWith(2));
  }
}
