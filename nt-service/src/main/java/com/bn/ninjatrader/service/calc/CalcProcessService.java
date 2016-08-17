package com.bn.ninjatrader.service.calc;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.google.inject.Inject;

import java.time.LocalDate;

import static com.bn.ninjatrader.process.request.CalcRequest.forStock;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcProcessService {

  @Inject
  private StockDao stockDao;

  @Inject
  @CalcAllProcess
  private CalcProcess calcProcess;

  public void processForAllSymbols(CalcProcess calcProcess, LocalDate fromDate, LocalDate toDate) {
    for (Stock stock : stockDao.find()) {
      calcProcess.processMissingBars(forStock(stock).from(fromDate).to(toDate));
    }
  }

  public void processAll(LocalDate fromDate, LocalDate toDate) {
    processForAllSymbols(calcProcess, fromDate, toDate);
  }
}
