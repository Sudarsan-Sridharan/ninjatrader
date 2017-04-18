package com.bn.ninjatrader.simulation.logicexpression.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.model.SimContext;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PortfolioHasSharesTest {

  private final PortfolioHasShares portfolioHasShares = PortfolioHasShares.instance();

  private BarData barData;
  private SimContext simContext;
  private Account account;
  private Portfolio portfolio;

  @Before
  public void before() {
    barData = mock(BarData.class);
    simContext = mock(SimContext.class);
    account = mock(Account.class);
    portfolio = mock(Portfolio.class);

    when(barData.getSimContext()).thenReturn(simContext);
    when(simContext.getAccount()).thenReturn(account);
    when(account.getPortfolio()).thenReturn(portfolio);
  }

  @Test
  public void testMatch_shouldReturnTrueIfAccountHasShares() {
    when(portfolio.isEmpty()).thenReturn(true);
    assertThat(portfolioHasShares.isMatch(barData)).isFalse();

    when(portfolio.isEmpty()).thenReturn(false);
    assertThat(portfolioHasShares.isMatch(barData)).isTrue();
  }

  @Test
  public void testToString_shouldReturnStringWithValue() {
    when(portfolio.isEmpty()).thenReturn(false);
    assertThat(portfolioHasShares.toString(barData)).contains("true");

    when(portfolio.isEmpty()).thenReturn(true);
    assertThat(portfolioHasShares.toString(barData)).contains("false");
  }

  @Test
  public void testEqualsWithDiffObjects_shouldReturnNotEqual() {
    assertThat(portfolioHasShares).isEqualTo(PortfolioHasShares.instance())
        .isNotEqualTo(null)
        .isNotEqualTo("");
  }

  @Test
  public void testHashCode_shouldReturnDiffHashCodesForDiffObjects() {
    assertThat(Sets.newHashSet(portfolioHasShares, PortfolioHasShares.instance()))
        .containsExactly(portfolioHasShares);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(portfolioHasShares);
    assertThat(om.readValue(json, SimulationCondition.class)).isEqualTo(portfolioHasShares);
  }
}
