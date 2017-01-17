package com.bn.ninjatrader.process.runner;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.process.calc.*;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/15/16.
 */
@Deprecated
public class CalcAllProcessRunner {
  private static final Logger log = LoggerFactory.getLogger(CalcAllProcessRunner.class);

  @Inject
  private CalcAllProcess calcAllProcess;

  @Inject
  private CalcWeeklyPriceProcess calcMeanProcess;

  @Inject
  private CalcSMAProcess calcSimpleAverageProcess;

  @Inject
  private CalcPriceChangeProcess calcPriceChangeProcess;

  @Inject
  private CalcWeeklyPriceProcess calcWeeklyPriceProcess;

  @Inject
  private CalcRSIProcess calcRSIProcess;

  @Inject
  private StockDao stockDao;

  public void run() {
    LocalDate fromDate = LocalDate.of(1900, 1, 1);
    LocalDate toDate = LocalDate.now();
    runProcess(calcRSIProcess, fromDate, toDate);
  }

  private void runProcess(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    log.info("Running: {}", calcProcess.getClass().getSimpleName());

    for (Stock stock : stockDao.find()) {
      calcProcess.process(
          CalcRequest.forStock(stock)
              .from(fromDate)
              .to(toDate));
    }
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtProcessModule(),
        new NtModelModule()
    );

    CalcAllProcessRunner runner = injector.getInstance(CalcAllProcessRunner.class);
    runner.run();
  }
}
