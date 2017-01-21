//package com.bn.ninjatrader.simulation.operation.function;
//
//import com.bn.ninjatrader.common.data.Price;
//import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
//import com.bn.ninjatrader.simulation.data.BarData;
//import com.bn.ninjatrader.simulation.data.BarDataFactory;
//import com.bn.ninjatrader.simulation.data.BarDataHistory;
//import com.bn.ninjatrader.simulation.operation.Operation;
//import com.bn.ninjatrader.simulation.operation.Variables;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Optional;
//
//import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.testng.Assert.assertEquals;
//
///**
// * Created by Brad on 8/29/16.
// */
//public class HistoryFunctionTest {
//
//  private final Price price1 = new Price(null, 1.1, 1.2, 1.3, 1.4, 10000);
//  private final Price price2 = new Price(null, 2.1, 2.2, 2.3, 2.4, 20000);
//  private final Price price3 = new Price(null, 3.1, 3.2, 3.3, 3.4, 30000);
//
//  private final Operation operation = PRICE_CLOSE;
//  private final Operation priceOpen = Variables.PRICE_OPEN;
//
//  private final BarData barData1 = BarData.builder().addData(PRICE_CLOSE, 1.4).build();
//  private final BarData barData2 = BarData.builder().addData(PRICE_CLOSE, 2.4).build();
//  private final BarData barData3 = BarData.builder().addData(PRICE_CLOSE, 3.4).build();
//
//  private PriceDataMapAdaptor priceDataMapAdaptor;
//  private BarDataHistory history;
//  private BarDataFactory barDataFactory;
//
//  @Before
//  public void setup() {
//    priceDataMapAdaptor = mock(PriceDataMapAdaptor.class);
//    history = mock(BarDataHistory.class);
//    barDataFactory = new BarDataFactory(priceDataMapAdaptor);
//  }
//
//  @Test
//  public void testGetHistoryValue() {
//    when(history.getNBarsAgo(1)).thenReturn(Optional.of(barData2));
//
//    final HistoryFunction function = new HistoryFunction(history, operation, 0);
//
//    assertThat(function.getValue(barData1))
//  }
//
//  @Test
//  public void testSingleHistoryFunction() {
//
//    HistoryFunction function = new HistoryFunction(history, operation, 0);
//    assertEquals(function.getValue(barData), 1.4);
//
//    function = new HistoryFunction(operation, 1);
//    assertEquals(function.getValue(barData), 0.0);
//  }
//
//  @Test
//  public void testMultiHistoryFunction() {
//    barDataFactory.createWithPriceAtIndex(price1);
//    barDataFactory.createWithPriceAtIndex(price2);
//    BarData barData = barDataFactory.createWithPriceAtIndex(price3);
//
//    HistoryFunction function = new HistoryFunction(history, operation, 0);
//    assertEquals(function.getValue(barData), 3.4);
//
//    function = new HistoryFunction(history, operation, 1);
//    assertEquals(function.getValue(barData), 2.4);
//
//    function = new HistoryFunction(history, operation, 2);
//    assertEquals(function.getValue(barData), 1.4);
//
//    function = new HistoryFunction(history, operation, 100);
//    assertEquals(function.getValue(barData), 0.0);
//  }
//}
