package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Brad on 8/18/16.
 */
public class SellOrderExecutorTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();
  private final BarData barData = BarData.builder().price(price).build();
  private final BarData submittedBarData = BarData.builder().price(price).build();
  private final Order order = BuyOrder.builder().cashAmount(100000).type(OrderTypes.marketOpen()).build();
  private final PendingOrder pendingOrder = PendingOrder.of(order, submittedBarData);

  private Account account;
  private BoardLotTable boardLotTable;
  private SellOrderExecutor executor;

  @Before
  public void setup() {
    account = Account.withStartingCash(0);
    account.addToPortfolio(Transaction.buy().price(0.5).shares(100000).build());
    boardLotTable = mock(BoardLotTable.class);
    executor = new SellOrderExecutor(boardLotTable);
  }

  @Test
  public void testExecute() {
    final SellTransaction transaction = executor.execute(account, pendingOrder, barData);

    assertValidTransaction(transaction);
    assertAccountPortfolioSold();
  }

  private void assertValidTransaction(final SellTransaction tnx) {
    assertThat(tnx).isNotNull();
    assertThat(tnx.getDate()).isEqualTo(now);
    assertThat(tnx.getNumOfShares()).isEqualTo(100000);
    assertThat(tnx.getTransactionType()).isEqualTo(TransactionType.SELL);
    assertThat(tnx.getValue()).isEqualTo(100000.0);
    assertThat(tnx.getProfit()).isEqualTo(50000.0);
    assertThat(tnx.getProfitPcnt()).isEqualTo(1.0);
  }

  private void assertAccountPortfolioSold() {
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getNumOfShares()).isEqualTo(0);
    assertThat(account.getAvgPrice()).isEqualTo(0.0);
  }
}
