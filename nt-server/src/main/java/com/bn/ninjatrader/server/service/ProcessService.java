package com.bn.ninjatrader.server.service;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.google.inject.Inject;

import java.time.LocalDate;

/**
 * Created by Brad on 6/8/16.
 */
public class ProcessService {

  @Inject
  private StockDao stockDao;

  @Inject
  @CalcAllProcess
  private CalcProcess calcProcess;

  public void processForAllSymbols(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    for (Stock stock : stockDao.find()) {
      calcProcess.process(stock.getSymbol(), fromDate, toDate);
    }
  }

  public void processAll(LocalDate fromDate, LocalDate toDate) {
    processForAllSymbols(calcProcess, fromDate, toDate);
  }
}
