package com.bn.ninjatrader.process.calc;

import java.time.LocalDate;

/**
 * Created by Brad on 6/12/16.
 */
public interface CalcProcess {

  void process(String symbol, LocalDate fromDate, LocalDate toDate);
}
