package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
@Singleton
public class CalcAllProcess implements CalcProcess {

  private static final Logger LOG = LoggerFactory.getLogger(CalcAllProcess.class);
  private static final String PROCESS_NAME = "all";

  private final List<CalcProcess> processList;

  @Inject
  public CalcAllProcess(final CalcWeeklyPriceProcess calcWeeklyPriceProcess,
                        final CalcPriceChangeProcess calcPriceChangeProcess,
                        final CalcMeanProcess calcMeanProcess,
                        final CalcSMAProcess calcSMAProcess,
                        final CalcEMAProcess calcEMAProcess,
                        final CalcRSIProcess calcRSIProcess) {
    processList = Lists.newArrayList(
        calcWeeklyPriceProcess,
        calcPriceChangeProcess,
        calcMeanProcess,
        calcSMAProcess,
        calcEMAProcess,
        calcRSIProcess);
  }

  @Override
  public void process(final CalcRequest calcRequest) {
    for (final CalcProcess process : processList) {
      process.process(calcRequest);
    }
  }

  @Override
  public String getProcessName() {
    return PROCESS_NAME;
  }

  public List<CalcProcess> getProcessList() {
    return Collections.unmodifiableList(processList);
  }
}
