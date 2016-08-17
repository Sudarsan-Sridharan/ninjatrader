package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.condition.Condition;
import com.bn.ninjatrader.testplay.parameter.Parameters;
import com.bn.ninjatrader.testplay.parameter.TestPlayParameters;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Simulation {

  private static final Logger log = LoggerFactory.getLogger(Simulation.class);

  private List<Price> priceList;
  private List<Ichimoku> ichimokuList;
  private final TestPlayParameters testPlayParameters;
  private final VirtualAccount account;
  private final Condition buyCondition;
  private final Condition sellCondition;

  public Simulation(TestPlayParameters testPlayParameters, List<Price> priceList) {
    this.testPlayParameters = testPlayParameters;
    this.priceList = priceList;
    this.account = VirtualAccount.startWithEquity(testPlayParameters.getStartingEquity());
    this.buyCondition = testPlayParameters.getBuyCondition();
    this.sellCondition = testPlayParameters.getSellCondition();

    checkValidConstructorParams();
  }

  private void checkValidConstructorParams() {
    Preconditions.checkNotNull(testPlayParameters);
    Preconditions.checkNotNull(buyCondition);
    Preconditions.checkNotNull(sellCondition);
    Preconditions.checkNotNull(account);
    Preconditions.checkArgument(testPlayParameters.getStartingEquity() > 0, "Starting equity must be > 0.");
    Preconditions.checkNotNull(priceList);
    Preconditions.checkArgument(priceList.size() > 0, "Price list must not be empty.");
  }

  public void play() {
    Parameters parameters = new Parameters();

    Iterator<Price> priceIter = priceList.iterator();
    Iterator<Ichimoku> ichimokuIter = ichimokuList.iterator();

    Price lastPrice = null;
    while (priceIter.hasNext()) {
      Price price = priceIter.next();
      Ichimoku ichimoku = ichimokuIter.next();

      parameters.clearValues().put(price).put(ichimoku);
      processBar(parameters);
      lastPrice = price;
    }

    account.sell(lastPrice);
    account.printStats();
  }

  private void processBar(Parameters parameters) {
    if (account.hasBought()) {
      processSell(parameters);
    } else {
      processBuy(parameters);
    }
  }

  private void processBuy(Parameters parameters) {
    if (buyCondition.isMatch(parameters)) {
      account.buy(parameters.getPrice());
    }
  }

  private void processSell(Parameters parameters) {
    if (sellCondition.isMatch(parameters)) {
      account.sell(parameters.getPrice());
    }
  }

  public double getStartingEquity() {
    return testPlayParameters.getStartingEquity();
  }

  public Condition getBuyCondition() {
    return testPlayParameters.getBuyCondition();
  }

  public Condition getSellCondition() {
    return testPlayParameters.getSellCondition();
  }

  public List<Price> getPriceList() {
    return priceList;
  }

  public List<Ichimoku> getIchimokuList() {
    return ichimokuList;
  }

  public void setIchimokuList(List<Ichimoku> ichimokuList) {
    this.ichimokuList = alignToPrices(ichimokuList, Ichimoku.empty());
  }

  private <T> List<T> alignToPrices(List<T> dataList, T emptyFill) {
    if (dataList == null) {
      return dataList;
    }

    for (int i = dataList.size(); i < priceList.size(); i++) {
      dataList.add(0, emptyFill);
    }
    return dataList;
  }
}
