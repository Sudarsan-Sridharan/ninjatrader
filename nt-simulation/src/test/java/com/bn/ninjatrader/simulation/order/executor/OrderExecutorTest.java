package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by Brad on 8/18/16.
 */
public class OrderExecutorTest {
  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();
  private final BoardLot boardLot1 = BoardLot.newLot().lot(1000).tick(0.1).build();
  private final BoardLot boardLot2 = BoardLot.newLot().lot(100).tick(0.1).build();

  private BoardLotTable boardLotTable;
  private Account account;

  private OrderExecutor executor;

  @Before
  public void before() {
    account = Account.withStartingCash(100000);
    account.addToPortfolio(Transaction.buy().price(1.0).shares(100000).build());
    boardLotTable = mock(BoardLotTable.class);

    when(boardLotTable.getBoardLot(1)).thenReturn(boardLot1);
    when(boardLotTable.getBoardLot(2)).thenReturn(boardLot1);
    when(boardLotTable.getBoardLot(5)).thenReturn(boardLot2);

    executor = new OrderExecutor(boardLotTable) {
      @Override
      public Transaction execute(Account account, Order order, BarData barData) {
        return null;
      }
    };
  }

  @Test
  public void testGetNumberOfSharesCanBuy() {
    long numOfShares = executor.getNumOfSharesCanBuyWithAmount(100000, 1);
    assertThat(numOfShares).isEqualTo(100000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(100000, 2);
    assertThat(numOfShares).isEqualTo(50000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(99999, 1);
    assertThat(numOfShares).isEqualTo(99000);

    numOfShares = executor.getNumOfSharesCanBuyWithAmount(99999, 5);
    assertThat(numOfShares).isEqualTo(19900);
  }

  @Test
  public void testCalculateProfit() {
    double profit = executor.calculateProfit(account, 2.0);
    assertThat(profit).isEqualTo(100000.0);

    profit = executor.calculateProfit(account, 2.5);
    assertThat(profit).isEqualTo(150000.0);

    profit = executor.calculateProfit(account, 3.0);
    assertThat(profit).isEqualTo(200000.0);
  }

  @Test
  public void testCalculateProfitAtLowerPrice() {
    double profit = executor.calculateProfit(account, 0.5);
    assertThat(profit).isEqualTo(-50000.0);
  }
}
