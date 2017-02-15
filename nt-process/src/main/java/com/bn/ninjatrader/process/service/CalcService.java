package com.bn.ninjatrader.process.service;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.annotation.AllCalcProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.bn.ninjatrader.process.request.CalcServiceRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class CalcService {
  private static final Logger LOG = LoggerFactory.getLogger(CalcService.class);

  private final PriceDao priceDao;
  private final Clock clock;
  private final List<CalcProcess> allCalcProcesses;
  private final Map<String, CalcProcess> processMap = Maps.newHashMap();

  @Inject
  public CalcService(@AllCalcProcess final List<CalcProcess> allCalcProcesses,
                     final Clock clock,
                     final PriceDao priceDao) {
    this.allCalcProcesses = allCalcProcesses;
    this.clock = clock;
    this.priceDao = priceDao;

    for (final CalcProcess calcProcess : allCalcProcesses) {
      processMap.put(calcProcess.getProcessName(), calcProcess);
    }
  }

  public void calc(final CalcServiceRequest req) {
    final LocalDate from = req.getFrom().orElse(LocalDate.now(clock));
    final LocalDate to = req.getTo().orElse(LocalDate.now(clock));

    // Create list of symbols to calc.
    final List<String> symbols = provideSymbols(req);

    // Create list of processes to run.
    final List<CalcProcess> calcProcesses = provideCalcProcesses(req);

    // Run each process
    for (final CalcProcess calcProcess : calcProcesses) {
      LOG.info("Calc [{}] from [{}] to [{}]", calcProcess.getProcessName(), from, to);
      calcProcess.process(CalcRequest.forSymbols(symbols).from(from).to(to).timeFrames(TimeFrame.ONE_DAY));
    }
  }

  /**
   * Return list of processes to run.
   * @param req
   * @return
   */
  private List<CalcProcess> provideCalcProcesses(final CalcServiceRequest req) {
    final List<CalcProcess> calcProcesses = Lists.newArrayList();
    if (req.isAllProcesses()) {
      calcProcesses.addAll(allCalcProcesses);
    } else {
      for (final String processName : req.getProcessNames()) {
        final CalcProcess calcProcess = processMap.get(processName);
        checkNotNull(calcProcess, "calcProcess with name [%s] is not found", processName);
        calcProcesses.add(calcProcess);
      }
    }
    return calcProcesses;
  }

  /**
   * Return list of symbols to process
   */
  private List<String> provideSymbols(final CalcServiceRequest req) {
    final List<String> symbols = Lists.newArrayList();
    if (req.isAllSymbols()) {
      symbols.addAll(priceDao.findAllSymbols());
    } else {
      symbols.addAll(req.getSymbols());
    }
    return symbols;
  }
}
