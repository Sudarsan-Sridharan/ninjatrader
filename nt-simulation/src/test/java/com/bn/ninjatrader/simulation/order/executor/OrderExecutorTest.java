package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
import com.bn.ninjatrader.simulation.order.PendingOrder;
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
  private final BoardLot boardLot1 = BoardLot.newLot().lot(1000).tick(0.1).build();
  private final BoardLot boardLot2 = BoardLot.newLot().lot(100).tick(0.1).build();

  private BoardLotTable boardLotTable;
  private Account account;
  private Portfolio portfolio;

  private OrderExecutor executor;

  @Before
  public void before() {
    account = mock(Account.class);
    portfolio = mock(Portfolio.class);
    boardLotTable = mock(BoardLotTable.class);

    when(account.getPortfolio()).thenReturn(portfolio);
    when(boardLotTable.getBoardLot(1)).thenReturn(boardLot1);
    when(boardLotTable.getBoardLot(2)).thenReturn(boardLot1);
    when(boardLotTable.getBoardLot(5)).thenReturn(boardLot2);

    executor = new OrderExecutor(boardLotTable) {
      @Override
      public Transaction execute(PendingOrder order, BarData barData) {
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
}
