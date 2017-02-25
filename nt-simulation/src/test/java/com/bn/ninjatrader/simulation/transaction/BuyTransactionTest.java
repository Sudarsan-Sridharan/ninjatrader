package com.bn.ninjatrader.simulation.transaction;

import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyTransactionTest {
  private static final Logger LOG = LoggerFactory.getLogger(BuyTransactionTest.class);

  private final ObjectMapper om = DummyObjectMapperProvider.get();
  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate tomorrow = LocalDate.of(2016, 1, 2);

  private final BuyTransaction orig = Transaction.buy().price(1).shares(10).date(now).barIndex(1).build();
  private final BuyTransaction same = Transaction.buy().price(1).shares(10).date(now).barIndex(1).build();
  private final BuyTransaction diff1 = Transaction.buy().price(2).shares(10).date(now).barIndex(1).build();
  private final BuyTransaction diff2 = Transaction.buy().price(1).shares(11).date(now).barIndex(1).build();
  private final BuyTransaction diff3 = Transaction.buy().price(1).shares(10).date(tomorrow).barIndex(1).build();
  private final BuyTransaction diff4 = Transaction.buy().price(1).shares(10).date(now).barIndex(2).build();
  private final SellTransaction diff5 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).build();

  @Test
  public void testCreateEmpty_shouldHaveDefaultValues() {
    BuyTransaction transaction = Transaction.buy().build();

    assertThat(transaction.getDate()).isNull();
    assertThat(transaction.getPrice()).isEqualTo(0.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testGettersSetters_shouldSetFieldsCorrectly() {
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();

    assertThat(transaction.getDate()).isEqualTo(now);
    assertThat(transaction.getPrice()).isEqualTo(100.0);
    assertThat(transaction.getNumOfShares()).isEqualTo(1000);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testGetValue_shouldCalculateTotalValue() {
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(100000d);

    transaction = Transaction.buy().date(now).price(0.0001).shares(1000).build();
    assertThat(transaction.getValue()).isEqualTo(0.1);
  }

  @Test
  public void testHashCode_shouldBeDiffForDiffObjects() {
    Set<Transaction> set = Sets.newHashSet();
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
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();
    String serialized = om.writeValueAsString(transaction);
    Transaction deserialized = om.readValue(serialized, Transaction.class);
    assertThat(deserialized).isInstanceOf(Transaction.class).isEqualTo(transaction);
  }
}
