package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.order.type.OrderTypes.marketClose;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyOrderTest {
  private static final Logger LOG = LoggerFactory.getLogger(BuyOrderTest.class);

  private LocalDate now = LocalDate.of(2016, 1, 1);
  private BuyOrder orig = BuyOrder.builder().date(now).symbol("MEG").cashAmount(100).type(marketClose()).build();
  private BuyOrder equal = BuyOrder.builder().date(now).symbol("MEG").cashAmount(100).type(marketClose()).build();
  private BuyOrder diffSymbol = BuyOrder.builder().date(now).symbol("BDO").cashAmount(100).type(marketClose()).build();
  private BuyOrder diffCashAmt = BuyOrder.builder().date(now).symbol("MEG").cashAmount(90).type(marketClose()).build();
  private BuyOrder diffMarketTime = BuyOrder.builder().date(now).symbol("MEG").cashAmount(100).type(OrderTypes.marketOpen()).build();
  private BuyOrder diffConfig = BuyOrder.builder().date(now).symbol("MEG")
      .cashAmount(100).type(marketClose()).config(OrderConfig.defaults().barsFromNow(1)).build();
  private BuyOrder diffNumOfShares = BuyOrder.builder().date(now).symbol("MEG")
      .cashAmount(100).type(marketClose()).shares(100).build();

  @Test
  public void testCreateEmpty_shouldCreateWithDefaultValues() {
    final BuyOrder order = BuyOrder.builder().build();
    assertThat(order.getSymbol()).isNullOrEmpty();
    assertThat(order.getOrderDate()).isNull();
    assertThat(order.getCashAmount()).isEqualTo(0.0);
    assertThat(order.getNumOfShares()).isEqualTo(0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testCreate_shouldHavePropertiesSet() {
    final BuyOrder order = BuyOrder.builder().date(now).symbol("TEL").type(marketClose()).shares(1000).cashAmount(20000)
        .config(OrderConfig.defaults()).build();
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getSymbol()).isEqualTo("TEL");
    assertThat(order.getOrderType()).isEqualTo(marketClose());
    assertThat(order.getNumOfShares()).isEqualTo(1000);
    assertThat(order.getCashAmount()).isEqualTo(20000.0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);
    assertThat(order.getOrderConfig()).isEqualTo(OrderConfig.defaults());
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffSymbol)
        .isNotEqualTo(diffCashAmt)
        .isNotEqualTo(diffMarketTime)
        .isNotEqualTo(diffConfig)
        .isNotEqualTo(diffNumOfShares);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffSymbol, diffConfig, diffCashAmt, diffMarketTime, diffNumOfShares))
        .containsExactlyInAnyOrder(orig, diffSymbol, diffConfig, diffCashAmt, diffMarketTime, diffNumOfShares);
  }
}
