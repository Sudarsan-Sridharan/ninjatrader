//package com.bn.ninjatrader.calculator;
//
//import com.bn.ninjatrader.logical.expression.operation.Operations;
//import com.bn.ninjatrader.model.entity.Price;
//import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
//import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
//import com.google.common.collect.Lists;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class PriceAdjustmentCalculatorTest {
//
//  private final LocalDate now = LocalDate.of(2016, 2, 2);
//  private final PriceBuilderFactory priceBuilderFactory = new DummyPriceBuilderFactory();
//  private final PriceAdjustmentCalculator calculator = new PriceAdjustmentCalculator(priceBuilderFactory);
//
//  @Test
//  public void testAdjustPrices_shouldAdjustPriceAttributes() {
//    final List<Price> prices = Lists.newArrayList(
//        priceBuilderFactory.builder()
//            .date(now).open(10).high(15).low(1).close(3).change(-0.3).volume(10000).build(),
//        priceBuilderFactory.builder()
//            .date(now).open(100).high(200).low(50).close(75).change(90).volume(20000).build()
//    );
//
//    assertThat(calculator.calc(prices, Operations.startWith(PriceAdjustmentCalculator.PRICE).div(5)))
//        .containsExactly(
//            priceBuilderFactory.builder()
//                .date(now).open(2).high(3).low(0.2).close(0.6).change(-0.06).volume(10000).build(),
//            priceBuilderFactory.builder()
//                .date(now).open(20).high(40).low(10).close(15).change(18).volume(20000).build()
//    );
//
//    assertThat(calculator.calc(prices, Operations.startWith(3).minus(1).mult(PriceAdjustmentCalculator.PRICE)))
//        .containsExactly(
//            priceBuilderFactory.builder()
//                .date(now).open(20).high(30).low(2).close(6).change(-0.6).volume(10000).build(),
//            priceBuilderFactory.builder()
//                .date(now).open(200).high(400).low(100).close(150).change(180).volume(20000).build()
//    );
//  }
//}
