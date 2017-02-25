package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipValue implements Operation<BarData> {
  private static final Logger LOG = LoggerFactory.getLogger(PipValue.class);
  private static final String TO_STRING_FORMAT = "%spip(%s)";

  public static final PipValue of(final int pip) {
    return new PipValue(pip);
  }

  @JsonProperty("pip")
  private final int pip;

  public PipValue(@JsonProperty("pip") final int pip) {
    Preconditions.checkArgument(pip > 0, "pip should be > 0.");
    this.pip = pip;
  }

  @Override
  public double getValue(final BarData barData) {
    final World world = barData.getWorld();
    final BoardLotTable boardLotTable = world.getBoardLotTable();
    final Price price = barData.getPrice();
    final double tick = boardLotTable.getBoardLot(price.getClose()).getTick();
    return tick * pip;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PipValue)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PipValue rhs = (PipValue) obj;
    return Objects.equal(pip, rhs.pip);
  }

  public int getPip() {
    return pip;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(pip);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("pip", pip).toString();
  }

  @Override
  public String toString(final BarData barData) {
    return String.format(TO_STRING_FORMAT, pip, NumUtil.trim(getValue(barData), 4));
  }
}
