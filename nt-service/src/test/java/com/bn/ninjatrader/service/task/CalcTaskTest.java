package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.calc.CalcAllProcess;
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

  private final CalcAllProcess calcAllProcess = mock(CalcAllProcess.class);
  private final CalcProcess process1 = mock(CalcProcess.class);
  private final CalcProcess process2 = mock(CalcProcess.class);

  private CalcTask calcTask;

  @Before
  public void before() {
    when(priceDao.findAllSymbols()).thenReturn(Lists.newArrayList("MEG"));
    when(printWriter.append(any())).thenReturn(printWriter);

    when(process1.getProcessName()).thenReturn("process1");
    when(process2.getProcessName()).thenReturn("process2");

    when(calcAllProcess.getProcessList()).thenReturn(Lists.newArrayList(process1, process2));
    when(calcAllProcess.getProcessName()).thenReturn("all");

    calcTask = new CalcTask(calcAllProcess, clock, priceDao);
  }

  @Test
  public void testExecuteAllProcess() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "all")
        .put("name", "process1")
        .build();

    calcTask.execute(map, printWriter);

    verify(calcAllProcess).process(any(CalcRequest.class));
    verify(process1, times(0)).process(any(CalcRequest.class));
  }

  @Test
  public void testExecuteMultipleProcesses() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "process1")
        .put("name", "process2")
        .build();

    calcTask.execute(map, printWriter);

    verify(process1).process(any(CalcRequest.class));
    verify(process2).process(any(CalcRequest.class));
  }

  @Test
  public void testArgumentsHandling() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("name", "process1")
        .put("from", "20160101")
        .put("to", "20170202")
        .build();
    final ArgumentCaptor<CalcRequest> calcRequestCaptor = ArgumentCaptor.forClass(CalcRequest.class);

    calcTask.execute(map, printWriter);

    verify(process1).process(calcRequestCaptor.capture());

    CalcRequest calcRequest = calcRequestCaptor.getValue();
    assertThat(calcRequest.getFromDate()).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(calcRequest.getToDate()).isEqualTo(LocalDate.of(2017, 2, 2));
  }

  @Test
  public void testDefaultSettings() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .build();
    final ArgumentCaptor<CalcRequest> calcRequestCaptor = ArgumentCaptor.forClass(CalcRequest.class);

    calcTask.execute(map, printWriter);

    verify(calcAllProcess).process(calcRequestCaptor.capture());

    CalcRequest calcRequest = calcRequestCaptor.getValue();
    assertThat(calcRequest.getFromDate()).isEqualTo(now);
    assertThat(calcRequest.getToDate()).isEqualTo(now);
  }

  @Test
  public void testCalcForSpecifiedSymbols() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder()
        .put("symbol", "BDO")
        .put("symbol", "CEB")
        .build();
    final ArgumentCaptor<CalcRequest> calcRequestCaptor = ArgumentCaptor.forClass(CalcRequest.class);

    calcTask.execute(map, printWriter);

    verify(calcAllProcess, times(2)).process(calcRequestCaptor.capture());
    assertThat(calcRequestCaptor.getAllValues()).hasSize(2).extracting("symbol").containsExactly("BDO", "CEB");
  }

  @Test
  public void testCalcForAllSymbols() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder().build();
    final ArgumentCaptor<CalcRequest> calcRequestCaptor = ArgumentCaptor.forClass(CalcRequest.class);

    when(priceDao.findAllSymbols()).thenReturn(Lists.newArrayList("MEG", "BDO", "TEL"));

    calcTask.execute(map, printWriter);

    verify(calcAllProcess, times(3)).process(calcRequestCaptor.capture());
    assertThat(calcRequestCaptor.getAllValues()).hasSize(3).extracting("symbol").containsExactly("MEG", "BDO", "TEL");
  }
}
