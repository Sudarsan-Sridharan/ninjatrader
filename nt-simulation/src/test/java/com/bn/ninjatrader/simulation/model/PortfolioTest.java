package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/23/16.
 */
public class PortfolioTest {

  private BuyTransaction buy1 = Transaction.buy().price(1).shares(10000).build();
  private BuyTransaction buy2 = Transaction.buy().price(2).shares(10000).build();
  private BuyTransaction buy3 = Transaction.buy().price(3).shares(1000).build();

  private Portfolio portfolio;

  @Before
  public void setup() {
    portfolio = new Portfolio();
  }

  @Test
  public void testOnCreate() {
    assertPortfolioIsEmpty();
  }

  @Test
  public void testAdd() {
    portfolio.add(buy1);
    assertThat(portfolio.isEmpty()).isFalse();
  }

  @Test
  public void testAvgPrice() {
    portfolio.add(buy1);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.0);

    portfolio.add(buy2);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.5);

    portfolio.add(buy3);
    assertThat(portfolio.getAvgPrice()).isEqualTo(1.571429);
  }

  @Test
  public void testNumOfShares() {
    portfolio.add(buy1);
    assertThat(portfolio.getTotalShares()).isEqualTo(10000);

    portfolio.add(buy2);
    assertThat(portfolio.getTotalShares()).isEqualTo(20000);

    portfolio.add(buy3);
    assertThat(portfolio.getTotalShares()).isEqualTo(21000);
  }

  @Test
  public void testClear() {
    portfolio.add(buy1);
    portfolio.clear();
    assertPortfolioIsEmpty();
  }

  private void assertPortfolioIsEmpty() {
    assertThat(portfolio.isEmpty()).isTrue();
    assertThat(portfolio.getAvgPrice()).isEqualTo(0.0);
    assertThat(portfolio.getTotalShares()).isEqualTo(0);
  }
}
