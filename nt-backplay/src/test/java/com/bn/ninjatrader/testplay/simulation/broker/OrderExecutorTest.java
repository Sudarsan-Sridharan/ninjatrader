package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.order.MarketTime;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public class OrderExecutorTest {

  @Injectable
  private BoardLotTable boardLotTable;

  @Tested
  private OrderExecutor executor;

  private Account account;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = new Price(now, 1, 2, 3, 4, 1000);
  private final BarData barData = new BarData().put(price).index(1);

  private final BoardLot boardLot1 = BoardLot.newLot().lot(1000).tick(0.1).build();
  private final BoardLot boardLot2 = BoardLot.newLot().lot(100).tick(0.1).build();

  private final Order orderForMarketOpen = Order.buy().cashAmount(100000).at(MarketTime.OPEN).build();
  private final Order orderForMarketClose = Order.buy().cashAmount(100000).at(MarketTime.CLOSE).build();

  @BeforeMethod
  public void setup() {
    account = Account.withStartingCash(100000);
    account.addToPortfolio(Transaction.buy().price(1.0).shares(100000).build());

    new Expectations() {{
      boardLotTable.getBoardLot(1);
      result = boardLot1;
      minTimes = 0;

      boardLotTable.getBoardLot(2);
      result = boardLot1;
      minTimes = 0;

      boardLotTable.getBoardLot(5);
      result = boardLot2;
      minTimes = 0;
    }};
  }

  @Test
  public void testGetFulfilledPrice() {
    double fulfilledPrice = executor.getFulfilledPrice(orderForMarketOpen, barData);
    assertEquals(fulfilledPrice, 1.0);

    fulfilledPrice = executor.getFulfilledPrice(orderForMarketClose, barData);
    assertEquals(fulfilledPrice, 4.0);
  }

  @Test
  public void testGetNumberOfSharesCanBuy() {
    long numOfShares = executor.getNumOfSharesCanBuyWithAmount(100000, 1);
    assertEquals(numOfShares, 100000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(100000, 2);
    assertEquals(numOfShares, 50000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(99999, 1);
    assertEquals(numOfShares, 99000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(99999, 5);
    assertEquals(numOfShares, 19900);
  }

  @Test
  public void testCalculateProfit() {
    double profit = executor.calculateProfit(account, 2.0);
    assertEquals(profit, 100000.0);

    profit = executor.calculateProfit(account, 2.5);
    assertEquals(profit, 150000.0);

    profit = executor.calculateProfit(account, 3.0);
    assertEquals(profit, 200000.0);
  }
}
