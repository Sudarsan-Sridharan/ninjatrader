package com.bn.ninjatrader.process.service;

import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.bn.ninjatrader.process.request.CalcServiceRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class CalcServiceTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final Clock clock = TestUtil.fixedClock(now);

  private CalcProcess process1, process2;
  private PriceDao priceDao;

  private CalcService calcService;

  @Before
  public void before() {
    process1 = mock(CalcProcess.class);
    process2 = mock(CalcProcess.class);
    priceDao = mock(PriceDao.class);

    when(process1.getProcessName()).thenReturn("process1");
    when(process2.getProcessName()).thenReturn("process2");

    calcService = new CalcService(Lists.newArrayList(process1, process2), clock, priceDao);
  }

  @Test
  public void testRunWithNoInputDate_shouldRunProcessForToday() {
    final ArgumentCaptor<CalcRequest> captor = ArgumentCaptor.forClass(CalcRequest.class);

    calcService.calc(CalcServiceRequest.builder().addProcessName("process1").build());

    verify(process1).process(captor.capture());
    assertThat(captor.getValue().getFromDate()).isEqualTo(now);
    assertThat(captor.getValue().getToDate()).isEqualTo(now);
  }

  @Test
  public void testRunWithMultiProcessAndSymbols_shouldRunForEachProcessAndSymbol() {
    final ArgumentCaptor<CalcRequest> captor1 = ArgumentCaptor.forClass(CalcRequest.class);
    final ArgumentCaptor<CalcRequest> captor2 = ArgumentCaptor.forClass(CalcRequest.class);

    calcService.calc(CalcServiceRequest.builder()
        .addProcessNames("process1", "process2").addSymbols("MEG", "BDO").build());

    verify(process1).process(captor1.capture());
    assertThat(captor1.getValue().getAllSymbols()).containsExactlyInAnyOrder("MEG", "BDO");

    verify(process2).process(captor2.capture());
    assertThat(captor2.getValue().getAllSymbols()).containsExactlyInAnyOrder("MEG", "BDO");
  }

  @Test
  public void testRunWithAllProcess_shouldRunAllProcesses() throws Exception {
    calcService.calc(CalcServiceRequest.builder().allProcesses().build());

    verify(process1).process(any(CalcRequest.class));
    verify(process2).process(any(CalcRequest.class));
  }

  @Test
  public void testRunWithDateRange_shouldIncludeFromToDates() throws Exception {
    final ArgumentCaptor<CalcRequest> captor = ArgumentCaptor.forClass(CalcRequest.class);
    final LocalDate from = LocalDate.of(2016, 1, 1);
    final LocalDate to = LocalDate.of(2016, 2, 5);

    calcService.calc(CalcServiceRequest.builder().addProcessName("process1").from(from).to(to).build());

    verify(process1).process(captor.capture());

    final CalcRequest calcRequest = captor.getValue();
    assertThat(calcRequest.getFromDate()).isEqualTo(from);
    assertThat(calcRequest.getToDate()).isEqualTo(to);
  }

  @Test
  public void testCalcForAllSymbols_shouldCalcForAllSymbols() throws Exception {
    final ArgumentCaptor<CalcRequest> captor = ArgumentCaptor.forClass(CalcRequest.class);

    when(priceDao.findAllSymbols()).thenReturn(Sets.newHashSet("MEG", "BDO", "TEL"));

    calcService.calc(CalcServiceRequest.builder().addProcessName("process1").allSymbols().build());

    // Verify all symbols submitted for processing
    verify(process1).process(captor.capture());
    assertThat(captor.getValue().getAllSymbols())
        .containsExactlyInAnyOrder("MEG", "BDO", "TEL");
  }
}
