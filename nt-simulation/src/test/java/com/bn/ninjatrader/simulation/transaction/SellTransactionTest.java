package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class SellTransactionTest {
  private static final Logger LOG = LoggerFactory.getLogger(SellTransactionTest.class);

  private final ObjectMapper om = TestUtil.objectMapper();
  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate tom = LocalDate.of(2016, 1, 2);

  private SellTransaction orig = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(100).build();
  private SellTransaction same = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(100).build();
  private SellTransaction diff1 = Transaction.sell().price(2).shares(10).date(now).barIndex(1).profit(100).build();
  private SellTransaction diff2 = Transaction.sell().price(1).shares(11).date(now).barIndex(1).profit(100).build();
  private SellTransaction diff3 = Transaction.sell().price(1).shares(10).date(tom).barIndex(1).profit(100).build();
  private SellTransaction diff4 = Transaction.sell().price(1).shares(10).date(now).barIndex(2).profit(100).build();
  private SellTransaction diff5 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(101).build();

  @Test
  public void testCreateEmpty_shouldHaveDefaultValuesSet() {
    SellTransaction transaction = Transaction.sell().build();

    assertThat(transaction.getDate()).isNull();
    assertThat(transaction.getPrice()).isEqualTo(0.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(0);
    assertThat(transaction.getProfit()).isEqualTo(0.0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.SELL);
  }

  @Test
  public void testGettersSetters_shouldHaveCorrectValuesSet() {
    SellTransaction transaction = Transaction.sell().date(now).price(100).shares(1000).profit(5000).build();

    assertThat(transaction.getDate()).isEqualTo(now);
    assertThat(transaction.getPrice()).isEqualTo(100.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(1000);
    assertThat(transaction.getProfit()).isEqualTo(5000.0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.SELL);
  }

  @Test
  public void testGetValue_shouldReturnCalculatedValue() {
    SellTransaction transaction = Transaction.sell().date(now).price(100).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(100000d);

    transaction = Transaction.sell().date(now).price(0.0001).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(0.1);
  }

  @Test
  public void testHashCode_shouldBeDiffForDiffObjects() {
    Set<SellTransaction> set = Sets.newHashSet();
    set.add(orig);
    set.add(same);
    set.add(diff1);
    set.add(diff2);
    set.add(diff3);
    set.add(diff4);
    set.add(diff5);
    assertThat(set).hasSize(6).containsOnly(orig, diff1, diff2, diff3, diff4, diff5);
  }

  @Test
  public void testEquals_shouldBeEqualForObjectsWithSameValues() {
    assertThat(orig)
        .isEqualTo(orig)
        .isEqualTo(same)
        .isNotEqualTo(new Object())
        .isNotEqualTo(null)
        .isNotEqualTo(diff1)
        .isNotEqualTo(diff2)
        .isNotEqualTo(diff3)
        .isNotEqualTo(diff4)
        .isNotEqualTo(diff5);
    assertThat(same).isEqualTo(orig);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    SellTransaction transaction = Transaction.sell().date(now).price(100).shares(1000).build();
    String serialized = om.writeValueAsString(transaction);
    Transaction deserialized = om.readValue(serialized, Transaction.class);
    assertThat(deserialized).isInstanceOf(SellTransaction.class).isEqualTo(transaction);
  }
}
