package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.*;
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

import static com.bn.ninjatrader.process.request.CalcRequest.forStock;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class CalcTask extends Task {

  private static final Logger LOG = LoggerFactory.getLogger(CalcTask.class);

  @Inject
  private StockDao stockDao;

  @Inject
  private Clock clock;

  private Map<String, CalcProcess> processMap = Maps.newHashMap();

  @Inject
  public CalcTask(@CalcAllProcess CalcProcess calcAllProcess,
                  CalcRSIProcess calcRSIProcess,
                  CalcMeanProcess calcMeanProcess,
                  CalcWeeklyPriceProcess calcWeeklyPriceProcess,
                  CalcPriceChangeProcess calcPriceChangeProcess) {
    super("calc");

    processMap.put("all", calcAllProcess);
    processMap.put("rsi", calcRSIProcess);
    processMap.put("mean", calcMeanProcess);
    processMap.put("weekly-price", calcWeeklyPriceProcess);
    processMap.put("price-change", calcPriceChangeProcess);
  }

  @Override
  public void execute(ImmutableMultimap<String, String> args, PrintWriter printWriter) throws Exception {
    LOG.info("Executing CalcALL {}", args);

    Collection<String> processNames = args.get("name");

    LocalDate fromDate = args.containsKey("from") ?
        LocalDate.parse(args.get("from").asList().get(0), DateFormats.DB_DATE_FORMAT) :
        LocalDate.now(clock).minusYears(2);

    LocalDate toDate = args.containsKey("to") ?
        LocalDate.parse(args.get("to").asList().get(0), DateFormats.DB_DATE_FORMAT) :
        LocalDate.now(clock);

    for (String processName : processNames) {
      CalcProcess calcProcess = processMap.get(processName);
      LOG.info("Executing {}", calcProcess.getClass().getSimpleName());
      processForAllSymbols(calcProcess, fromDate, toDate);
    }
  }

  public void processForAllSymbols(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    for (Stock stock : stockDao.find()) {
      calcProcess.process(forStock(stock).from(fromDate).to(toDate));
    }
  }
}
