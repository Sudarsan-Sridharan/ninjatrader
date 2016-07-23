package com.bn.ninjatrader.process.calc;

import mockit.Mocked;
import mockit.Verifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;

/**
 * Created by Brad on 6/11/16.
 */
public class SequentialCalcProcessTest {

  private static final Logger log = LoggerFactory.getLogger(SequentialCalcProcessTest.class);

  @Mocked
  private CalcProcess calcProcess1;

  @Mocked
  private CalcProcess calcProcess2;

  @Test
  public void testProcess() {
    LocalDate fromDate = LocalDate.of(2015, 1, 1);
    LocalDate toDate = LocalDate.of(2017, 2, 2);

    SequentialCalcProcess process = SequentialCalcProcess.newInstance(calcProcess1, calcProcess2);

    process.process("MEG", fromDate, toDate);

    new Verifications() {{
      calcProcess1.process("MEG", fromDate, toDate); times = 1;
      calcProcess2.process("MEG", fromDate, toDate); times = 1;
    }};

  }
}
