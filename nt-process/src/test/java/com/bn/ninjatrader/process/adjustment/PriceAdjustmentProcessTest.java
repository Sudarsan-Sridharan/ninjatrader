package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
  public void testRunWithAllSymbols_shouldRetrieveSymbolsFromDatabase() {
    when(priceDao.findAllSymbols()).thenReturn(Sets.newHashSet("MEG", "BDO"));

    process.process(PriceAdjustmentRequest.forAllSymbols().from(from).to(to).adjustment(Operations.startWith(2)));

    verify(priceDao).findAllSymbols();

    verify(priceDao, times(2)).find(any(FindRequest.class));
    verify(priceDao, times(2)).save(any(SaveRequest.class));
  }

  @Test
  public void testRunWithSymbol_shouldCalculateForSymbol() {
    final ArgumentCaptor<FindRequest> findCaptor = ArgumentCaptor.forClass(FindRequest.class);
    final ArgumentCaptor<SaveRequest> saveCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    final Price price = mock(Price.class);
    final Price adjustedPrice = mock(Price.class);

    when(priceDao.find(any(FindRequest.class))).thenReturn(Lists.newArrayList(price));
    when(calculator.calc(any(List.class), any(Operation.class))).thenReturn(Lists.newArrayList(adjustedPrice));

    process.process(PriceAdjustmentRequest.forSymbol("MEG").from(from).to(to).adjustment(Operations.startWith(2)));

    // Verify find all symbols not called
    verify(priceDao, times(0)).findAllSymbols();

    // Verify FindRequest
    verify(priceDao).find(findCaptor.capture());
    final FindRequest findRequest = findCaptor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("MEG");
    assertThat(findRequest.getFromDate()).isEqualTo(from);
    assertThat(findRequest.getToDate()).isEqualTo(to);
    assertThat(findRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify SaveRequest
    verify(priceDao).save(saveCaptor.capture());
    final SaveRequest saveRequest = saveCaptor.getValue();
    assertThat(saveRequest.getSymbol()).isEqualTo("MEG");
    assertThat(saveRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify Calculator called
    verify(calculator).calc(Lists.newArrayList(price), Operations.startWith(2));
  }
}
