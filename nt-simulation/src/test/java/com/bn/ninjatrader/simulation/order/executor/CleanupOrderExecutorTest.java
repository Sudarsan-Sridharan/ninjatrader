package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/18/16.
 */
public class CleanupOrderExecutorTest {
  private static final Logger LOG = LoggerFactory.getLogger(CleanupOrderExecutorTest.class);

  private final LocalDate now = LocalDate.of(2016, 1, 1);

  private final SellTransaction sellTxn = SellTransaction.builder()
      .date(now).profit(100000).profitPcnt(0.5).barIndex(2500).shares(3000).build();

  private BarData bar;
  private PendingOrder pendingOrder;
  private BoardLotTable boardLotTable;
  private SellOrderExecutor sellOrderExecutor;
  private CleanupOrderExecutor cleanupOrderExecutor;

  @Before
  public void setup() {
    bar = mock(BarData.class, RETURNS_DEEP_STUBS);
    pendingOrder = mock(PendingOrder.class, RETURNS_MOCKS);
    boardLotTable = mock(BoardLotTable.class);
    sellOrderExecutor = mock(SellOrderExecutor.class);

    when(sellOrderExecutor.execute(any(), any())).thenReturn(sellTxn);

    cleanupOrderExecutor = new CleanupOrderExecutor(boardLotTable, sellOrderExecutor);
  }

  @Test
  public void testExecute_shouldCreateSellTransactionMarkedAsCleanup() {
    when(pendingOrder.getOrder()).thenReturn(mock(SellOrder.class));

    final SellTransaction txn = cleanupOrderExecutor.execute(pendingOrder, bar);

    assertThat(txn.getTransactionType()).isEqualTo(TransactionType.CLEANUP);
    assertThat(txn.getProfit()).isEqualTo(100000);
    assertThat(txn.getProfitPcnt()).isEqualTo(0.5);
    assertThat(txn.getDate()).isEqualTo(now);
    assertThat(txn.getBarIndex()).isEqualTo(2500);
    assertThat(txn.getNumOfShares()).isEqualTo(3000l);
  }

  @Test
  public void testExecute_shouldSellAllSharesInPortfolio() {
    final ArgumentCaptor<PendingOrder> orderCaptor = ArgumentCaptor.forClass(PendingOrder.class);

    when(pendingOrder.getOrder()).thenReturn(mock(SellOrder.class));
    when(bar.getSimulationContext().getAccount().getPortfolio().getTotalShares()).thenReturn(3000l);

    cleanupOrderExecutor.execute(pendingOrder, bar);

    verify(sellOrderExecutor).execute(orderCaptor.capture(), any(BarData.class));

    assertThat(orderCaptor.getValue().getNumOfShares()).isEqualTo(3000l);
  }
}
