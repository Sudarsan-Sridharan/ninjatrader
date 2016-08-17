package com.bn.ninjatrader.testplay.simulation.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class TradeStatistic {

  private static final Logger log = LoggerFactory.getLogger(TradeStatistic.class);

  private int numOfTrades = 0;
  private int numOfWins = 0;
  private int numOfLosses = 0;

  public void trade() {
    numOfTrades++;
  }

  public void win() {
    numOfWins++;
  }

  public void lose() {
    numOfLosses++;
  }

  public int getNumOfTrades() {
    return numOfTrades;
  }

  public int getNumOfWins() {
    return numOfWins;
  }

  public int getNumOfLosses() {
    return numOfLosses;
  }

  public double getWinLoseRation() {
    return (double) (numOfWins) / numOfLosses;
  }

  public double getProfitPerTrade(double profit) {
    return profit / numOfTrades;
  }
}
