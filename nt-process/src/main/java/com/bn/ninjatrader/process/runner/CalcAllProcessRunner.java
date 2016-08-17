package com.bn.ninjatrader.process.runner;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.bn.ninjatrader.process.calc.CalcSimpleAverageProcess;
import com.bn.ninjatrader.process.calc.CalcWeeklyPriceProcess;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.time.LocalDate;

/**
 * Created by Brad on 8/15/16.
 */
public class CalcAllProcessRunner {

  @Inject
  @CalcAllProcess
  private CalcProcess calcAllProcess;

  @Inject
  private CalcWeeklyPriceProcess calcMeanProcess;

  @Inject
  private CalcSimpleAverageProcess calcSimpleAverageProcess;

  @Inject
  private CalcWeeklyPriceProcess calcWeeklyPriceProcess;

  @Inject
  private StockDao stockDao;

  public void run() {
    LocalDate fromDate = LocalDate.of(1900, 1, 1);
    LocalDate toDate = LocalDate.now();
    runProcess(calcAllProcess, fromDate, toDate);
  }

  private void runProcess(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    for (Stock stock : stockDao.find()) {
      calcProcess.processMissingBars(
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
