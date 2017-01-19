package com.bn.ninjatrader.simulation.order;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Set;

import static com.bn.ninjatrader.simulation.order.MarketTime.CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyOrderTest {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrderTest.class);
  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty_shouldCreateWithDefaultValues() {
    BuyOrder order = Order.buy().build();
    assertThat(order.getOrderDate()).isNull();
    assertThat(order.getCashAmount()).isEqualTo(0.0);
    assertThat(order.getNumOfShares()).isEqualTo(0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testCreate_shouldHaveValuesSetProperly() {
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).cashAmount(20000).barsFromNow(5).build();
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getMarketTime()).isEqualTo(CLOSE);
    assertThat(order.getNumOfShares()).isEqualTo(1000);
    assertThat(order.getCashAmount()).isEqualTo(20000.0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
    assertThat(order.isReadyForProcessing()).isFalse();
  }

  @Test
  public void testBuyDaysFromNow_shouldBeReadyForProcessingTheNextDay() {
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).barsFromNow(1).build();
    assertFalse(order.isReadyForProcessing());

    order.decrementBarsFromNow();
    assertThat(order.isReadyForProcessing()).isTrue();
  }

  @Test
  public void testEqualsWithSameObjects_shouldBeEqual() {
    BuyOrder order = Order.buy().cashAmount(1000).build();
    assertThat(order).isEqualTo(order);
  }

  @Test
  public void testHashCodeWithDiffObjects_shouldHaveDiffHashCodes() {
    Set<BuyOrder> set = Sets.newHashSet();
    set.add(Order.buy().build());
    set.add(Order.buy().cashAmount(10).build());
    set.add(Order.buy().date(LocalDate.now()).build());
    set.add(Order.buy().at(MarketTime.OPEN).build());
    set.add(Order.buy().barsFromNow(1).build());
    set.add(Order.buy().shares(100).build());

    assertThat(set).hasSize(6);
  }

  @Test
  public void testHashCodeWithSameObjects_shouldHaveEqualHashCodes() {
    Set<BuyOrder> set = Sets.newHashSet();
    set.add(Order.buy().cashAmount(10).date(LocalDate.now()).at(MarketTime.OPEN).barsFromNow(10).shares(1000).build());
    set.add(Order.buy().cashAmount(10).date(LocalDate.now()).at(MarketTime.OPEN).barsFromNow(10).shares(1000).build());
    assertThat(set).hasSize(1);
  }
}
