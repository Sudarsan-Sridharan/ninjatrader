package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.process.request.CalcRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Brad on 6/11/16.
 */
public class SequentialCalcProcessTest {
  private static final Logger LOG = LoggerFactory.getLogger(SequentialCalcProcessTest.class);

  private CalcProcess calcProcess1;
  private CalcProcess calcProcess2;

  @Before
  public void setup() {
    calcProcess1 = mock(CalcProcess.class);
    calcProcess2 = mock(CalcProcess.class);
  }

  @Test
  public void testGetProcessName_shouldReturnProcessName() {
    final SequentialCalcProcess process = SequentialCalcProcess.newInstance("test", calcProcess1);
    assertThat(process.getProcessName()).isEqualTo("test");
  }

  @Test
  public void testCreateWithMultipleProcesses_shouldAddAllProcesses() {
    final SequentialCalcProcess process = SequentialCalcProcess.newInstance("test", calcProcess1, calcProcess2);
    assertThat(process).containsExactly(calcProcess1, calcProcess2);
  }

  @Test
  public void testAddingNullProcess_shouldThrowException() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() ->
        SequentialCalcProcess.newInstance("test", null));
  }

  @Test
  public void testAddingMoreNullProcess_shouldThrowException() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() ->
        SequentialCalcProcess.newInstance("test", calcProcess1, null, null));
  }

  @Test
  public void testExecute_shouldExecuteAllProcesses() {
    final LocalDate fromDate = LocalDate.of(2015, 1, 1);
    final LocalDate toDate = LocalDate.of(2017, 2, 2);
    final SequentialCalcProcess process = SequentialCalcProcess.newInstance("test", calcProcess1, calcProcess2);
    final CalcRequest request = CalcRequest.forSymbol("MEG").from(fromDate).to(toDate);

    process.process(request);

    verify(calcProcess1).process(request);
    verify(calcProcess2).process(request);
  }
}
