package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.MarketTime;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 8/18/16.
 */
public class BuyOrderExecutorTest {

  @Injectable
  private BoardLotTable boardLotTable;

  @Tested
  private BuyOrderExecutor executor;

  private Account account;
  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();
  private final BarData barData = BarData.builder().price(price).build();
  private final BoardLot boardLot = BoardLot.newLot().lot(1000).tick(0.1).build();
  private final Order order = Order.buy().cashAmount(100000).at(MarketTime.OPEN).build();

  @BeforeMethod
  public void setup() {
    account = Account.withStartingCash(100000);

    new Expectations() {{
      boardLotTable.getBoardLot(anyDouble);
      result = boardLot;
    }};
  }

  @Test
  public void testExecute() {
    final BuyTransaction transaction = executor.execute(account, order, barData);

    assertValidTransaction(transaction);
    assertBuyAddedToAccount();
  }

  private void assertValidTransaction(BuyTransaction transaction) {
    assertNotNull(transaction);
    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getNumOfShares(), 100000);
    assertEquals(transaction.getTransactionType(), TransactionType.BUY);
    assertEquals(transaction.getValue(), 100000.0);
  }

  private void assertBuyAddedToAccount() {
    assertEquals(account.getCash(), 0d);
    assertEquals(account.getNumOfShares(), 100000);
    assertEquals(account.getAvgPrice(), 1.0);
  }
}
