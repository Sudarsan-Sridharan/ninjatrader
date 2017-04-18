package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Created by Brad on 8/23/16.
 */
public class PortfolioTest {

  private final BuyTransaction buy1 = Transaction.buy().symbol("MEG").price(1).shares(10000).build();
  private final BuyTransaction buy2 = Transaction.buy().symbol("MEG").price(2).shares(10000).build();
  private final BuyTransaction buy3 = Transaction.buy().symbol("MEG").price(3).shares(1000).build();

  private final BuyTransaction buyDiffSymbol = Transaction.buy().symbol("BDO").price(1).shares(1000).build();

  private Portfolio portfolio;

  @Before
  public void setup() {
    portfolio = new Portfolio();
  }

  @Test
  public void testOnCreate() {
    assertThat(portfolio.isEmpty()).isTrue();
    assertThat(portfolio.getTotalEquityValue()).isEqualTo(0);
  }

  @Test
  public void testAdd_shouldAddTransactionToPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.isEmpty()).isFalse();
  }

  @Test
  public void testAvgPrice_shouldReturnAveragePriceOfStockInPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.getAvgPrice("MEG")).isEqualTo(1.0);

    portfolio.add(buy2);
    assertThat(portfolio.getAvgPrice("MEG")).isEqualTo(1.5);

    portfolio.add(buy3);
    portfolio.add(buyDiffSymbol);
    assertThat(portfolio.getAvgPrice("MEG")).isEqualTo(1.5714285714285714);
    assertThat(portfolio.getAvgPrice("BDO")).isEqualTo(1);

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.getAvgPrice("URC"))
        .withMessageContaining("URC");
  }

  @Test
  public void testNumOfShares_shouldReturnTotalSharesInPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(10000);

    portfolio.add(buy2);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(20000);

    portfolio.add(buy3);
    portfolio.add(buyDiffSymbol);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(21000);
    assertThat(portfolio.getTotalShares("BDO")).isEqualTo(1000);

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.getTotalShares("URC"))
        .withMessageContaining("URC");
  }

  @Test
  public void testGetEquityValue_shouldReturnTotalEquitValue() {
    portfolio.add(buy1);
    assertThat(portfolio.getEquityValue("MEG")).isEqualTo(10000);

    portfolio.add(buy2);
    assertThat(portfolio.getEquityValue("MEG")).isEqualTo(30000);

    portfolio.add(buy3);
    portfolio.add(buyDiffSymbol);
    assertThat(portfolio.getEquityValue("MEG")).isEqualTo(33000);
    assertThat(portfolio.getEquityValue("BDO")).isEqualTo(1000);

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.getEquityValue("URC"))
        .withMessageContaining("URC");
  }

  @Test
  public void testClear_shouldRemoveAllSharesFromPortfolio() {
    portfolio.add(buy1);
    portfolio.clear();
    assertThat(portfolio.isEmpty()).isTrue();
    assertThat(portfolio.contains("MEG")).isFalse();
  }

  @Test
  public void testCommitShares_shouldAddSharesToNumOfCommittedShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);

    // Can commit 5000 shares
    assertThat(portfolio.canCommitShares("MEG", 5000)).isTrue();
    portfolio.commitShares("MEG", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(5000);

    // Can commit another 5000 more shares
    assertThat(portfolio.canCommitShares("MEG", 5000)).isTrue();
    portfolio.commitShares("MEG", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(10000);

    // Can't commit any more shares.
    assertThat(portfolio.canCommitShares("MEG", 1)).isFalse();

    // Can't commit any more shares. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> portfolio.commitShares("MEG", 1));

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> portfolio.commitShares("URC", 100));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> portfolio.getCommittedShares("URC"));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> portfolio.canCommitShares("URC", 100));
  }

  @Test
  public void testCancelCommittedShares_shouldDeductFromCommittedShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    portfolio.commitShares("MEG", 5000);

    // Cancel partial
    portfolio.cancelCommittedShares("MEG", 1000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(4000);

    // Cancel all
    portfolio.cancelCommittedShares("MEG", 4000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);

    // Nothing left. Can't cancel anymore
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.cancelCommittedShares("MEG", 1));

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.cancelCommittedShares("URC", 1));
  }

  @Test
  public void testFulfillCommittedShares_shouldDeductFromTotalShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    portfolio.commitShares("MEG", 5000);

    // Fulfill partial
    portfolio.fulfillCommittedShares("MEG", 1000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(4000);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(9000);

    // Fulfill all
    portfolio.fulfillCommittedShares("MEG", 4000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(5000);

    // Nothing left to fulfill. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.fulfillCommittedShares("MEG", 1));

    // URC symbol doesn't exist. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> portfolio.fulfillCommittedShares("URC", 1));
  }

  @Test
  public void testCommitAndFulfillAllSharesOfStock_shouldRemoveItemFromPortfolio() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());

    portfolio.commitShares("MEG", 10000);
    portfolio.fulfillCommittedShares("MEG", 10000);

    assertThat(portfolio.isEmpty());
  }

  @Test
  public void testTotalEquityValue_shouldReturnSumOfAllItemEquityValues() {
    assertThat(portfolio.getTotalEquityValue()).isEqualTo(0);

    portfolio.add(Transaction.buy().symbol("MEG").price(2).shares(10000).build());
    assertThat(portfolio.getTotalEquityValue()).isEqualTo(20000);

    portfolio.add(Transaction.buy().symbol("BDO").price(3).shares(1000).build());
    assertThat(portfolio.getTotalEquityValue()).isEqualTo(23000);
  }
}
