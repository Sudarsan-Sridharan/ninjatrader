package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class SellOrderTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty_shouldSetDefaultValuesToProperties() {
    final SellOrder order = SellOrder.builder().build();
    assertThat(order.getOrderDate()).isNull();
    assertThat(order.getNumOfShares()).isEqualTo(0);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.SELL);
  }

  @Test
  public void testCreate_shouldSetProperties() {
    final SellOrder order = SellOrder.builder().date(now).type(OrderTypes.marketClose()).shares(1000).build();
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getOrderType()).isEqualTo(OrderTypes.marketClose());
    assertThat(order.getNumOfShares()).isEqualTo(1000);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.SELL);
    assertThat(order.getOrderConfig()).isEqualTo(OrderConfig.defaults());
  }
}
