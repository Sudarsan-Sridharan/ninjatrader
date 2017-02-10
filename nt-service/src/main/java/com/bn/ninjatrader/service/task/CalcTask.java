package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.annotation.AllCalcProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bn.ninjatrader.process.request.CalcRequest.calcSymbol;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * By default, executes all calculations for today
 * curl -X POST localhost:8081/tasks/calc
 *
 * To execute all calculations for specific dates:
 * curl -X POST localhost:8081/tasks/calc -d "name=all&from=19900101&to=20200101"
 *
 * To execute multiple specific calculations:
 * curl -X POST localhost:8081/tasks/calc -d "name=rsi&name=mean&from=19900101&to=20200101"
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
  private final List<CalcProcess> allCalcProcesses;
  private final Map<String, CalcProcess> processMap = Maps.newHashMap();

  @Inject
  public CalcTask(@AllCalcProcess final List<CalcProcess> allCalcProcesses,
                  final Clock clock,
                  final PriceDao priceDao) {
    super(TASK_NAME);
    this.allCalcProcesses = allCalcProcesses;
    this.clock = clock;
    this.priceDao = priceDao;

    for (final CalcProcess calcProcess : allCalcProcesses) {
      processMap.put(calcProcess.getProcessName(), calcProcess);
    }
  }

  @Override
  public void execute(final ImmutableMultimap<String, String> args, final PrintWriter printWriter) {
    LOG.debug("Executing task with arguments", args);

    try {
      final Collection<String> inputProcessNames = args.get(ARG_PROCESS_NAME);
      final Collection<String> inputSymbols = args.get(ARG_SYMBOL);
      final LocalDate from = getDateFromArgs(args, ARG_FROM_DATE).orElse(LocalDate.now(clock));
      final LocalDate to = getDateFromArgs(args, ARG_TO_DATE).orElse(LocalDate.now(clock));

      // Create list of processes to run.
      final List<CalcProcess> calcProcesses = Lists.newArrayList();
      if (inputProcessNames.isEmpty() || inputProcessNames.contains("all")) {
        calcProcesses.addAll(allCalcProcesses);
      } else {
        for (final String processName : inputProcessNames) {
          final CalcProcess calcProcess = processMap.get(processName);
          checkNotNull(calcProcess, "calcProcess with name [%s] is not found", processName);
          calcProcesses.add(calcProcess);
        }
      }

      // Create list of symbols to calc.
      final List<String> symbols = Lists.newArrayList();
      if (inputSymbols == null || inputSymbols.isEmpty()) {
        symbols.addAll(priceDao.findAllSymbols());
      } else {
        symbols.addAll(inputSymbols);
      }

      runEachCalcProcessForEachSymbol(calcProcesses, symbols, from, to);

      printWriter.append(MSG_SUCCESS).append("\n");
    } catch (final Exception e) {
      printWriter.append(MSG_FAIL).append("\n");
      throw new RuntimeException(e);
    }
  }

  private Optional<LocalDate> getDateFromArgs(final ImmutableMultimap<String, String> args, final String name) {
    if (args.containsKey(name)) {
      return Optional.of(LocalDate.parse(args.get(name).asList().get(0), DateFormats.DB_DATE_FORMAT));
    }
    return Optional.empty();
  }

  private void runEachCalcProcessForEachSymbol(final Collection<CalcProcess> calcProcesses,
                                               final Collection<String> symbols,
                                               final LocalDate fromDate,
                                               final LocalDate toDate) {
    for (final CalcProcess calcProcess : calcProcesses) {
      for (final String symbol : symbols) {
        LOG.info("Calc [{}] for symbol [{}] from [{}] to [{}]", calcProcess.getProcessName(), symbol, fromDate, toDate);
        calcProcess.process(calcSymbol(symbol).from(fromDate).to(toDate).timeFrames(TimeFrame.ONE_DAY));
      }
    }
  }
}
