package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.model.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class SellTransactionTest {
  private static final Logger LOG = LoggerFactory.getLogger(SellTransactionTest.class);

  private final ObjectMapper om = TestUtil.objectMapper();
  private final LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty_shouldHaveDefaultValuesSet() {
    final SellTransaction transaction = SellTransaction.builder().build();

    assertThat(transaction.getDate()).isNull();
    assertThat(transaction.getPrice()).isEqualTo(0.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(0);
    assertThat(transaction.getProfit()).isEqualTo(0.0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.SELL);
  }

  @Test
  public void testCopy_shouldCreateEqualCopy() {
    final SellTransaction original = SellTransaction.builder()
        .date(now).price(100).shares(1000).profit(5000).profitPcnt(0.05).build();

    final SellTransaction copy = SellTransaction.builder().copyFrom(original).build();

    assertThat(copy).isEqualTo(original);
  }

  @Test
  public void testGettersSetters_shouldHaveCorrectValuesSet() {
    final SellTransaction transaction = SellTransaction.builder().date(now).price(100).shares(1000).profit(5000).build();

    assertThat(transaction.getDate()).isEqualTo(now);
    assertThat(transaction.getPrice()).isEqualTo(100.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(1000);
    assertThat(transaction.getProfit()).isEqualTo(5000.0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.SELL);
  }

  @Test
  public void testGetValue_shouldReturnCalculatedValue() {
    SellTransaction transaction = SellTransaction.builder().date(now).price(100).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(100000d);

    transaction = SellTransaction.builder().date(now).price(0.0001).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(0.1);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final SellTransaction transaction = SellTransaction.builder().date(now).price(100).shares(1000).build();
    final String serialized = om.writeValueAsString(transaction);
    final Transaction deserialized = om.readValue(serialized, Transaction.class);
    assertThat(deserialized).isInstanceOf(Transaction.class).isEqualTo(transaction);
  }
}
