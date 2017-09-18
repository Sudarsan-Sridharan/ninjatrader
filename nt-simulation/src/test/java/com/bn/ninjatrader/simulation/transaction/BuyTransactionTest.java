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
public class BuyTransactionTest {
  private static final Logger LOG = LoggerFactory.getLogger(BuyTransactionTest.class);

  private final ObjectMapper om = TestUtil.objectMapper();
  private final LocalDate now = LocalDate.of(2016, 1, 1);

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
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();
    String serialized = om.writeValueAsString(transaction);
    Transaction deserialized = om.readValue(serialized, Transaction.class);
    assertThat(deserialized).isInstanceOf(Transaction.class).isEqualTo(transaction);
  }
}
