package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.process.request.CalcRequest;
import mockit.Mocked;
import mockit.Verifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.process.request.CalcRequest.forSymbol;

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

    CalcRequest request = forSymbol("MEG").from(fromDate).to(toDate);
    process.processMissingBars(request);

    new Verifications() {{
      calcProcess1.processMissingBars(request); times = 1;
      calcProcess2.processMissingBars(request); times = 1;
    }};

  }
}
