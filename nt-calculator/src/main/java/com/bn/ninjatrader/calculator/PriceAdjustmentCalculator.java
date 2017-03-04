package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceAdjustmentCalculator {

  public static final Variable PRICE = Variable.of("PRICE");

  private enum Attr {
    OPEN, HIGH, LOW, CLOSE, CHANGE
  }

  private final PriceBuilderFactory priceBuilderFactory;

  @Inject
  public PriceAdjustmentCalculator(final PriceBuilderFactory priceBuilderFactory) {
    this.priceBuilderFactory = priceBuilderFactory;
  }

  public List<Price> calc(final List<Price> priceList, final Operation adjustment) {
    checkNotNull(adjustment, "adjustment operation must not be null.");
    checkArgument(adjustment.getVariables().contains(PRICE), "adjustment operation must use only variable %s.", PRICE);

    final List<Price> adjustedPrices = Lists.newArrayListWithCapacity(priceList.size());
    final PriceData priceData = new PriceData();

    for (final Price price : priceList) {
      final Price adjustedPrice = adjustPrice(priceData.price(price), adjustment);
      adjustedPrices.add(adjustedPrice);
    }
    return adjustedPrices;
  }

  /**
   * Adjust price based on adjustment formula.
   * @param priceData
   * @param adj
   * @return
   */
  private Price adjustPrice(final PriceData priceData, final Operation adj) {
    return priceBuilderFactory.builder().copyOf(priceData.getPrice())
        .open(adj.getValue(priceData.attr(Attr.OPEN)))
        .high(adj.getValue(priceData.attr(Attr.HIGH)))
        .low(adj.getValue(priceData.attr(Attr.LOW)))
        .close(adj.getValue(priceData.attr(Attr.CLOSE)))
        .change(adj.getValue(priceData.attr(Attr.CHANGE)))
        .build();
  }

  /**
   * Price Data wrapper
   */
  private static class PriceData implements Data {
    private Price price;
    private Attr attr;

    @Override
    public Double get(final Variable variable) {
      if (PRICE.equals(variable)) {
        switch (attr) {
          case OPEN: return price.getOpen();
          case HIGH: return price.getHigh();
          case LOW: return price.getLow();
          case CLOSE: return price.getClose();
          case CHANGE: return price.getChange();
        }
      }
      throw new IllegalStateException("Unknown variable: " + variable);
    }

    public PriceData price(final Price price) {
      this.price = price;
      return this;
    }

    public PriceData attr(final Attr attr) {
      this.attr = attr;
      return this;
    }

    public Price getPrice() {
      return price;
    }
  }
}
