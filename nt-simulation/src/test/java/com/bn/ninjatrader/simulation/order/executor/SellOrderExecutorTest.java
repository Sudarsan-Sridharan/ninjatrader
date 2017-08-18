package com.bn.ninjatrader.simulation.order.executor;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/18/16.
 */
public class SellOrderExecutorTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();
  private final BarData submittedBarData = BarData.builder().price(price).build();
  private final Order order = BuyOrder.builder().cashAmount(100000).type(OrderTypes.marketOpen()).build();
  private final PendingOrder pendingOrder = PendingOrder.of(order, submittedBarData);

  private BarData barData;
  private SimulationContext simulationContext;
  private Account account;
  private Portfolio portfolio;
  private BoardLotTable boardLotTable;
  private SellOrderExecutor executor;

  @Before
  public void setup() {
    barData = mock(BarData.class);
    simulationContext = mock(SimulationContext.class);
    account = mock(Account.class);
    portfolio = mock(Portfolio.class);
    boardLotTable = mock(BoardLotTable.class);

    when(barData.getPrice()).thenReturn(price);
    when(barData.getSimulationContext()).thenReturn(simulationContext);
    when(barData.getSymbol()).thenReturn("MEG");
    when(simulationContext.getAccount()).thenReturn(account);
    when(account.getPortfolio()).thenReturn(portfolio);

    executor = new SellOrderExecutor(boardLotTable);
  }

  @Test
  public void testExecute_shouldCreateSellTransactionWithCalcProfit() {
    when(portfolio.getTotalShares("MEG")).thenReturn(100000l);
    when(portfolio.getEquityValue("MEG")).thenReturn(100000d);
    when(portfolio.getAvgPrice("MEG")).thenReturn(0.75);

    final SellTransaction txn = executor.execute(pendingOrder, barData);

    assertThat(txn).isNotNull();
    assertThat(txn.getDate()).isEqualTo(now);
    assertThat(txn.getNumOfShares()).isEqualTo(100000);
    assertThat(txn.getTransactionType()).isEqualTo(TransactionType.SELL);
    assertThat(txn.getValue()).isEqualTo(100000.0);
    assertThat(txn.getProfit()).isEqualTo(25000);
    assertThat(txn.getProfitPcnt()).isEqualTo(0.25);
  }
}
