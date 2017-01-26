package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.model.MarketTime;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.model.MarketTime.CLOSE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyOrderTest {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrderTest.class);

  private LocalDate now = LocalDate.of(2016, 1, 1);
  private BuyOrder orig = BuyOrder.buy().date(now).cashAmount(100).at(MarketTime.CLOSE).barsFromNow(2).build();
  private BuyOrder equal = BuyOrder.buy().date(now).cashAmount(100).at(MarketTime.CLOSE).barsFromNow(2).build();
  private BuyOrder diffCashAmt = BuyOrder.buy().date(now).cashAmount(90).at(MarketTime.CLOSE).barsFromNow(2).build();
  private BuyOrder diffMarketTime = BuyOrder.buy()
      .date(now).cashAmount(100).at(MarketTime.OPEN).barsFromNow(2).build();
  private BuyOrder diffBarsFromNow = BuyOrder.buy()
      .date(now).cashAmount(100).at(MarketTime.CLOSE).barsFromNow(9).build();
  private BuyOrder diffNumOfShares = BuyOrder.buy().date(now)
      .cashAmount(100).at(MarketTime.CLOSE).barsFromNow(2).shares(100).build();

  @Test
  public void testCreateEmpty_shouldCreateWithDefaultValues() {
    final BuyOrder order = Order.buy().build();
    assertThat(order.getOrderDate()).isNull();
    assertThat(order.getCashAmount()).isEqualTo(0.0);
    assertThat(order.getNumOfShares()).isEqualTo(0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testCreate_shouldHavePropertiesSet() {
    final BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).cashAmount(20000).barsFromNow(5).build();
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getMarketTime()).isEqualTo(CLOSE);
    assertThat(order.getNumOfShares()).isEqualTo(1000);
    assertThat(order.getCashAmount()).isEqualTo(20000.0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffCashAmt)
        .isNotEqualTo(diffMarketTime)
        .isNotEqualTo(diffBarsFromNow)
        .isNotEqualTo(diffNumOfShares);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffBarsFromNow, diffCashAmt, diffMarketTime, diffNumOfShares))
        .containsExactlyInAnyOrder(orig, diffBarsFromNow, diffCashAmt, diffMarketTime, diffNumOfShares);
  }
}
