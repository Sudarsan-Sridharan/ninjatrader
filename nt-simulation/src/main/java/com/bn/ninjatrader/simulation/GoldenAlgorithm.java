package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.logicexpression.operation.*;
import com.bn.ninjatrader.simulation.logicexpression.statement.*;
import com.bn.ninjatrader.simulation.model.Marker;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.AtPrice;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.google.inject.Singleton;

import java.time.LocalDate;

import static com.bn.ninjatrader.logical.expression.condition.Conditions.*;
import static com.bn.ninjatrader.simulation.logicexpression.Variables.*;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class GoldenAlgorithm {

  public static GoldenAlgorithm newInstance() {
    return new GoldenAlgorithm();
  }

  private int PULLBACK_MIN_BAR = 2;
  private int PULLBACK_NUM_OF_BARS = 4;
  private int BUY_PIPS_BUFFER = 2;
  private int SELL_PIPS_BUFFER = 2;
  private double PULLBACK_SENSITIVITY = 0.005;
  private double MAX_BUY_RISK = 0.05;
  private LocalDate from = LocalDate.now().minusYears(10);
  private LocalDate to = LocalDate.now();

  public GoldenAlgorithm from(final LocalDate from) {
    this.from = from;
    return this;
  }

  public GoldenAlgorithm to(final LocalDate to) {
    this.to = to;
    return this;
  }

  public GoldenAlgorithm pullbackMinBar(final int pullbackMinBar) {
    PULLBACK_MIN_BAR = pullbackMinBar;
    return this;
  }

  public GoldenAlgorithm pullbackNumOfBars(final int pullbackNumOfBars) {
    PULLBACK_NUM_OF_BARS = pullbackNumOfBars;
    return this;
  }

  public GoldenAlgorithm buyPipsBuffer(final int buyPipsBuffer) {
    BUY_PIPS_BUFFER = buyPipsBuffer;
    return this;
  }

  public GoldenAlgorithm sellPipsBuffer(final int sellPipsBuffer) {
    SELL_PIPS_BUFFER = sellPipsBuffer;
    return this;
  }

  public GoldenAlgorithm pullbackSensitivity(final double pullbackSensitivity) {
    PULLBACK_SENSITIVITY = pullbackSensitivity;
    return this;
  }

  public GoldenAlgorithm maxBuyRisk(final double maxBuyRisk) {
    MAX_BUY_RISK = maxBuyRisk;
    return this;
  }

  public SimulationParams.Builder forSymbol(final String symbol) {
    final SimulationParams.Builder paramsBuilder = SimulationParams.builder()
        .symbol(symbol)
        .from(from)
        .to(to)
        .startingCash(100000)
        .algorithm(SimTradeAlgorithm.builder()
            .play(MultiStatement.of(
                // Initialize
                ConditionalStatement.withName("Initialize")
                    .condition(eq(BAR_INDEX, 1))
                    .then(SetPropertyStatement.builder()
                        .add("IS_TAKE_PROFIT_ON", 1)
                        .add("PULLBACK", 0)
                        .add("TRAILING_STOP", 0)
                        .add("TOP", 0)
                        .build()),

                // Pullback Condition - if higher than trailing stop, move trailing stop up.
                ConditionalStatement.withName("Pullback")
                    .condition(Conditions.and(
                        eq(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR), LowestValue.of(PRICE_LOW).fromBarsAgo(PULLBACK_NUM_OF_BARS)),
                        lt(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_NUM_OF_BARS)),
                        gte(PcntChangeValue.of(PRICE_LOW, HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY),
                        Conditions.or(
                            gte(PcntChangeValue.of(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(3), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY),
                            gte(PcntChangeValue.of(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY),
                            gte(PcntChangeValue.of(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(6), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY)
                        )
                    ))
                    .then(MultiStatement.of(
                        SetPropertyStatement.builder()
                            .add("PULLBACK", HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR))
                            .build(),
                        ConditionalStatement.withName("Move Trailing Stop Higher")
                            .condition(gt(PropertyValue.of("PULLBACK"), PropertyValue.of("TRAILING_STOP")))
                            .then(SetPropertyStatement.builder().add("TRAILING_STOP", PropertyValue.of("PULLBACK")).build()),
                        MarkStatement.withColor("blue").numOfBarsAgo(PULLBACK_MIN_BAR).marker(Marker.ARROW_BOTTOM)
                    )),

                // Buy Condition -- With 5% risk
                ConditionalStatement.withName("Buy with 5% risk")
                    .condition(Conditions.and(
                        lte(PcntChangeValue.of(Operations.startWith(PRICE_HIGH).plus(PipValue.of(BUY_PIPS_BUFFER)), LowestValue.of(PRICE_LOW).fromBarsAgo(6)), MAX_BUY_RISK),
                        gt(EMA.withPeriod(18), 0), // EMA 18 > 0. to make sure we have data.
                        Conditions.or(
                            gt(PRICE_CLOSE, EMA.withPeriod(18)) // Price above EMA 18
                        )
                    ))
                    .then(MultiStatement.of(
                        BuyOrderStatement.builder()
                            .orderType(AtPrice.of(Operations.startWith(PRICE_HIGH).plus(PipValue.of(BUY_PIPS_BUFFER))))
                            .orderConfig(OrderConfig.withBarsFromNow(1).expireAfterNumOfBars(1))
                            .build(),
                        MarkStatement.withColor("green").marker(Marker.ARROW_TOP),
                        ConditionalStatement.withName("Turn of take profit if bought too high")
                            .condition(gte(PcntChangeValue.of(Operations.startWith(PRICE_HIGH).plus(PipValue.of(BUY_PIPS_BUFFER)), EMA.withPeriod(18)), 0.16))
                            .then(SetPropertyStatement.builder()
                                .add("IS_TAKE_PROFIT_ON", 0)
                                .build())
                            .otherwise(SetPropertyStatement.builder()
                                .add("IS_TAKE_PROFIT_ON", 1)
                                .build())
                        )
                    ),

                // Price Below Trailing Stop -- Sell
                ConditionalStatement.withName("Sell")
                    .condition(Conditions.or(
                        lt(PRICE_LOW, Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(SELL_PIPS_BUFFER)))
                    ))
                    .then(ConditionalStatement.withName("Check if gap down")
                        .condition(lt(PRICE_HIGH, Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(SELL_PIPS_BUFFER))))
                        .then(
                            SellOrderStatement.builder() // Protection from gap down.
                                .orderType(OrderTypes.marketClose())
                                .build()
                        )
                        .otherwise(MultiStatement.of(
                            SellOrderStatement.builder() // Sell at price N pips below trailing stop
                                .orderType(AtPrice.of(Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(SELL_PIPS_BUFFER))))
                                .orderConfig(OrderConfig.withExpireAfterNumOfBars(2))
                                .build()
                        ))
                    ),

                // If price above EMA 18 by over 16%
                ConditionalStatement.withName("Take Profit")
                    .condition(Conditions.and(
                        eq(PropertyValue.of("IS_TAKE_PROFIT_ON"), 1),
                        gte(Operations.startWith(PRICE_HIGH).minus(EMA.withPeriod(18)).div(EMA.withPeriod(18)), 0.16)
                    ))
                    .then(MultiStatement.of(
                        SellOrderStatement.builder()
                            .orderType(AtPrice.of(HighestValue.of(PRICE_LOW, Operations.startWith(EMA.withPeriod(18)).mult(1.16))))
                            .orderConfig(OrderConfig.withExpireAfterNumOfBars(2))
                            .build()
                    ))

                // Set TRAILING STOP Upon Buy
            )).onBuyFulfilled(MultiStatement.of(
                SetPropertyStatement.builder()
                    .add("TRAILING_STOP", LowestValue.of(PRICE_LOW).fromBarsAgo(6))
                    .build()
            )).build()
        );

    return paramsBuilder;
  }
}
