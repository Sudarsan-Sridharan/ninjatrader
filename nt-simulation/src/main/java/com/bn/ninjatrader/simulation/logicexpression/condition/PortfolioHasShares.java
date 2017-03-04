package com.bn.ninjatrader.simulation.logicexpression.condition;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Portfolio;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortfolioHasShares implements SimulationCondition {
  private static final String TO_STRING = "accountHasShares(%s)";

  private static PortfolioHasShares INSTANCE;

  public static final PortfolioHasShares instance() {
    if (INSTANCE == null) {
      INSTANCE = new PortfolioHasShares();
    }
    return INSTANCE;
  }

  @Override
  public boolean isMatch(final BarData barData) {
    final World world = barData.getWorld();
    final Account account = world.getAccount();
    final Portfolio portfolio = account.getPortfolio();
    return !portfolio.isEmpty();
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PortfolioHasShares)) { return false; }
    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString(final BarData barData) {
    return String.format(TO_STRING, isMatch(barData));
  }
}
