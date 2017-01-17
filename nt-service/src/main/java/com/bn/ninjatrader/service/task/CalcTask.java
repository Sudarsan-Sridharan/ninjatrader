package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.calc.CalcAllProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.bn.ninjatrader.process.request.CalcRequest.calcSymbol;

/**
 * By default, executes all calculations for today
 * curl -X POST localhost:8081/tasks/calc
 *
 * To execute all calculations for specific dates:
 * curl -X POST localhost:8081/tasks/calc -d "name=all&from=19900101&to=20160101"
 *
 * To execute multiple specific calculations:
 * curl -X POST localhost:8081/tasks/calc -d "name=rsi&name=mean&from=19900101&to=20160101"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Timed
public class CalcTask extends Task {

  private static final Logger LOG = LoggerFactory.getLogger(CalcTask.class);
  private static final String TASK_NAME = "calc";

  private static final String ARG_PROCESS_NAME = "name";
  private static final String ARG_SYMBOL = "symbol";
  private static final String ARG_FROM_DATE = "from";
  private static final String ARG_TO_DATE = "to";

  private static final String MSG_SUCCESS = "Done";
  private static final String MSG_FAIL = "Failed";


  private final PriceDao priceDao;
  private final Clock clock;
  private final CalcAllProcess calcAllProcess;
  private final Map<String, CalcProcess> processMap = Maps.newHashMap();

  @Inject
  public CalcTask(final CalcAllProcess calcAllProcess,
                  final Clock clock,
                  final PriceDao priceDao) {
    super(TASK_NAME);
    this.calcAllProcess = calcAllProcess;
    this.clock = clock;
    this.priceDao = priceDao;

    processMap.put(calcAllProcess.getProcessName(), calcAllProcess);

    for (CalcProcess calcProcess : calcAllProcess.getProcessList()) {
      processMap.put(calcProcess.getProcessName(), calcProcess);
    }
  }

  @Override
  public void execute(ImmutableMultimap<String, String> args, PrintWriter printWriter) throws Exception {
    LOG.debug("Executing task with arguments", args);

    try {
      Collection<String> processNames = args.get(ARG_PROCESS_NAME);
      Collection<String> symbols = args.get(ARG_SYMBOL);

      LocalDate from = getDateFromArgs(args, ARG_FROM_DATE).orElse(LocalDate.now(clock));
      LocalDate to = getDateFromArgs(args, ARG_TO_DATE).orElse(LocalDate.now(clock));

      if (processNames.isEmpty() || processNames.contains(calcAllProcess.getProcessName())) {
        runProcess(calcAllProcess.getProcessName(), symbols, from, to);
      } else {
        runProcess(processNames, symbols, from, to);
      }

      printWriter.append(MSG_SUCCESS).append("\n");
    } catch (Exception e) {
      printWriter.append(MSG_FAIL).append("\n");
      throw e;
    }
  }

  private void runProcess(String processName, Collection<String> symbols, LocalDate from, LocalDate to) {
    CalcProcess calcProcess = processMap.get(processName);
    if (calcProcess != null) {
      LOG.info("Executing {} process: {}", processName, calcProcess);
      if (symbols == null || symbols.isEmpty()) {
        calcForAllSymbols(calcProcess, from, to);
      } else {
        calcForSymbols(calcProcess, symbols, from, to);
      }
    }
  }

  private void runProcess(Collection<String> processNames, Collection<String> symbols, LocalDate from, LocalDate to) {
    for (String processName : processNames) {
      runProcess(processName, symbols, from, to);
    }
  }

  private Optional<LocalDate> getDateFromArgs(ImmutableMultimap<String, String> args, String name) {
    if (args.containsKey(name)) {
      return Optional.of(LocalDate.parse(args.get(name).asList().get(0), DateFormats.DB_DATE_FORMAT));
    }
    return Optional.empty();
  }

  private void calcForAllSymbols(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    calcForSymbols(calcProcess, priceDao.findAllSymbols(), fromDate, toDate);
  }

  private void calcForSymbols(CalcProcess calcProcess,
                              Collection<String> symbols,
                              LocalDate fromDate,
                              LocalDate toDate) {
    for (String symbol : symbols) {
      LOG.debug("calc : {} -- {} to {}", calcProcess.getProcessName(), fromDate, toDate);
      calcProcess.process(calcSymbol(symbol).from(fromDate).to(toDate));
    }
  }
}
