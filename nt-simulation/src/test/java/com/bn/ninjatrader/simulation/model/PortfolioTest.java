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

  private final BuyTransaction buy1 = Transaction.buy().price(1).shares(10000).build();
  private final BuyTransaction buy2 = Transaction.buy().price(2).shares(10000).build();
  private final BuyTransaction buy3 = Transaction.buy().price(3).shares(1000).build();

  private Portfolio portfolio;

  @Before
  public void setup() {
    portfolio = new Portfolio();
  }

  @Test
  public void testOnCreate() {
    assertThat(portfolio.isEmpty()).isTrue();
    assertThat(portfolio.getAvgPrice()).isEqualTo(0.0);
    assertThat(portfolio.getTotalShares()).isEqualTo(0);
    assertThat(portfolio.getEquityValue()).isEqualTo(0);
  }

  @Test
  public void testAdd_shouldAddTransactionToPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.isEmpty()).isFalse();
  }

  @Test
  public void testAvgPrice_shouldReturnAveragePriceOfAllInPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.0);

    portfolio.add(buy2);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.5);

    portfolio.add(buy3);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.5714285714285714);
  }

  @Test
  public void testNumOfShares_shouldReturnTotalSharesInPortfolio() {
    portfolio.add(buy1);
    assertThat(portfolio.getTotalShares()).isEqualTo(10000);

    portfolio.add(buy2);
    assertThat(portfolio.getTotalShares()).isEqualTo(20000);

    portfolio.add(buy3);
    assertThat(portfolio.getTotalShares()).isEqualTo(21000);
  }

  @Test
  public void testGetEquityValue_shouldReturnTotalEquitValue() {
    portfolio.add(buy1);
    assertThat(portfolio.getEquityValue()).isEqualTo(10000);

    portfolio.add(buy2);
    assertThat(portfolio.getEquityValue()).isEqualTo(30000);

    portfolio.add(buy3);
    assertThat(portfolio.getEquityValue()).isEqualTo(33000);
  }

  @Test
  public void testClear_shouldRemoveAllSharesFromPortfolio() {
    portfolio.add(buy1);
    portfolio.clear();
    assertThat(portfolio.isEmpty()).isTrue();
    assertThat(portfolio.getAvgPrice()).isEqualTo(0.0);
    assertThat(portfolio.getTotalShares()).isEqualTo(0);
    assertThat(portfolio.getEquityValue()).isEqualTo(0);
  }

  @Test
  public void testCommitShares_shouldAddSharesToNumOfCommittedShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);

    // BDO doesn't exist
    portfolio.commitShares("BDO", 10000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);
    assertThat(portfolio.getCommittedShares("BDO")).isEqualTo(0);

    portfolio.commitShares("MEG", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(5000);

    portfolio.commitShares("MEG", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(10000);

    // Can't commit any more shares. Should throw exception.
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      portfolio.commitShares("MEG", 1);
    });
  }

  @Test
  public void testCancelCommittedShares_shouldDeductFromCommittedShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    portfolio.commitShares("MEG", 5000);

    // BDO doesn't exist
    portfolio.cancelCommittedShares("BDO", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(5000);

    // Cancel partial
    portfolio.cancelCommittedShares("MEG", 1000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(4000);

    // Cancel all
    portfolio.cancelCommittedShares("MEG", 4000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);

    // Nothing left. Can't cancel anymore
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      portfolio.cancelCommittedShares("MEG", 1);
    });
  }

  @Test
  public void testFulfillCommittedShares_shouldDeductFromTotalShares() {
    portfolio.add(Transaction.buy().symbol("MEG").price(1).shares(10000).build());
    portfolio.commitShares("MEG", 5000);

    // BDO doesn't exist
    portfolio.fulfillCommittedShares("BDO", 5000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(5000);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(10000);

    // Fulfill partial
    portfolio.fulfillCommittedShares("MEG", 1000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(4000);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(9000);

    // Fulfill all
    portfolio.fulfillCommittedShares("MEG", 4000);
    assertThat(portfolio.getCommittedShares("MEG")).isEqualTo(0);
    assertThat(portfolio.getTotalShares("MEG")).isEqualTo(5000);

    // Nothing left to fulfill
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      portfolio.fulfillCommittedShares("MEG", 1);
    });
  }

}
