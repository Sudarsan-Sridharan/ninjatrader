package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class CalcTaskTest {

  private final PriceDao priceDao = mock(PriceDao.class);
  private final PrintWriter printWriter = mock(PrintWriter.class);
  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final Clock clock = TestUtil.fixedClock(now);

  private final CalcProcess process1 = mock(CalcProcess.class);
  private final CalcProcess process2 = mock(CalcProcess.class);
  private final List<CalcProcess> allCalcProcesses = Lists.newArrayList(process1, process2);

  private CalcTask calcTask;

  @Before
  public void before() {
    when(priceDao.findAllSymbols()).thenReturn(Lists.newArrayList("MEG"));
    when(printWriter.append(any())).thenReturn(printWriter);

    when(process1.getProcessName()).thenReturn("process1");
    when(process2.getProcessName()).thenReturn("process2");

    calcTask = new CalcTask(allCalcProcesses, clock, priceDao);
  }

  @Test
  public void testExecuteAllProcess_shouldExecuteEachProcess() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "all")
        .put("name", "process1")
        .build();

    calcTask.execute(map, printWriter);

    verify(process1).process(any(CalcRequest.class));
    verify(process2).process(any(CalcRequest.class));
  }

  @Test
  public void testExecuteMultipleProcesses_shouldRunEachNamedProcess() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "process1")
        .put("name", "process2")
        .build();

    calcTask.execute(map, printWriter);

    verify(process1).process(any(CalcRequest.class));
    verify(process2).process(any(CalcRequest.class));
  }

  @Test
  public void testWithFromTo_shouldIncludeFromToDates() throws Exception {
    final ArgumentCaptor<CalcRequest> calcRequestCaptor = ArgumentCaptor.forClass(CalcRequest.class);
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "process1")
        .put("from", "20160101")
        .put("to", "20170202")
        .build();

    calcTask.execute(map, printWriter);

    verify(process1).process(calcRequestCaptor.capture());

    final CalcRequest calcRequest = calcRequestCaptor.getValue();
    assertThat(calcRequest.getFromDate()).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(calcRequest.getToDate()).isEqualTo(LocalDate.of(2017, 2, 2));
  }

  @Test
  public void testDefaultSettings_shouldCalcForTodayDate() throws Exception {
    final ArgumentCaptor<CalcRequest> calcReqCaptor1 = ArgumentCaptor.forClass(CalcRequest.class);
    final ArgumentCaptor<CalcRequest> calcReqCaptor2 = ArgumentCaptor.forClass(CalcRequest.class);
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .build();

    calcTask.execute(map, printWriter);

    // Verify process1 is run with today's date
    verify(process1).process(calcReqCaptor1.capture());
    final CalcRequest calcRequest = calcReqCaptor1.getValue();
    assertThat(calcRequest.getFromDate()).isEqualTo(now);
    assertThat(calcRequest.getToDate()).isEqualTo(now);

    // Verify process2 is run with today's date
    verify(process2).process(calcReqCaptor2.capture());
    final CalcRequest calcRequest2 = calcReqCaptor2.getValue();
    assertThat(calcRequest2.getFromDate()).isEqualTo(now);
    assertThat(calcRequest2.getToDate()).isEqualTo(now);
  }

  @Test
  public void testCalcForSpecifiedSymbols_shouldCalcForEachInputSymbols() throws Exception {
    final ArgumentCaptor<CalcRequest> calcReqCaptor1 = ArgumentCaptor.forClass(CalcRequest.class);
    final ArgumentCaptor<CalcRequest> calcReqCaptor2 = ArgumentCaptor.forClass(CalcRequest.class);
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("symbol", "BDO")
        .put("symbol", "CEB")
        .build();

    // Run
    calcTask.execute(map, printWriter);

    // Verify process1 is run with input symbols
    verify(process1, times(2)).process(calcReqCaptor1.capture());
    assertThat(calcReqCaptor1.getAllValues()).hasSize(2).extracting("symbol").containsExactly("BDO", "CEB");

    // Verify process2 is run with input symbols
    verify(process2, times(2)).process(calcReqCaptor2.capture());
    assertThat(calcReqCaptor2.getAllValues()).hasSize(2).extracting("symbol").containsExactly("BDO", "CEB");
  }

  @Test
  public void testCalcForAllSymbols_shouldCalcForAllSymbols() throws Exception {
    final ArgumentCaptor<CalcRequest> calcRequestCaptor1 = ArgumentCaptor.forClass(CalcRequest.class);
    final ArgumentCaptor<CalcRequest> calcRequestCaptor2 = ArgumentCaptor.forClass(CalcRequest.class);
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder().build();

    when(priceDao.findAllSymbols()).thenReturn(Lists.newArrayList("MEG", "BDO", "TEL"));

    // Run
    calcTask.execute(map, printWriter);

    // Verify process1 is run for each symbol
    verify(process1, times(3)).process(calcRequestCaptor1.capture());
    assertThat(calcRequestCaptor1.getAllValues()).hasSize(3).extracting("symbol").containsExactly("MEG", "BDO", "TEL");

    // Verify process2 is run for each symbol
    verify(process2, times(3)).process(calcRequestCaptor2.capture());
    assertThat(calcRequestCaptor2.getAllValues()).hasSize(3).extracting("symbol").containsExactly("MEG", "BDO", "TEL");
  }
}
