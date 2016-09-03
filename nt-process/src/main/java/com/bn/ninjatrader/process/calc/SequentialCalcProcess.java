package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
public class SequentialCalcProcess implements CalcProcess {

  private static final Logger log = LoggerFactory.getLogger(SequentialCalcProcess.class);

  public static final SequentialCalcProcess newInstance(CalcProcess calcProcess1, CalcProcess ... calcProcesses) {
    return new SequentialCalcProcess(calcProcess1, calcProcesses);
  }

  private List<CalcProcess> processList;

  public SequentialCalcProcess(CalcProcess calcProcess1, CalcProcess ... calcProcesses) {
    processList = Lists.asList(calcProcess1, calcProcesses);
  }

  @Override
  public void processMissingBars(CalcRequest calcRequest) {
    for (CalcProcess process : processList) {
      process.processMissingBars(calcRequest);
    }
  }
}
