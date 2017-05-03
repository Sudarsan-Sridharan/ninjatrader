//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.ValueCalculator;
//import com.bn.ninjatrader.calculator.parameter.CalcParams;
//import com.bn.ninjatrader.common.data.Price;
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.common.type.TimeFrame;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.mongo.dao.ValueDao;
//import com.bn.ninjatrader.adjustPrices.provider.PriorValueProvider;
//import com.bn.ninjatrader.adjustPrices.provider.PriorValueRequest;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class AbstractCalcContinuedValueProcessTest {
//  private LocalDate now = LocalDate.of(2016, 2, 1);
//  private ValueCalculator calculator;
//  private PriceDao priceDao;
//  private ValueDao emaDao;
//  private PriorValueProvider priorValueProvider;
//
//  private DummyProcess adjustPrices;
//
//  @Before
//  public void before() {
//    calculator = mock(ValueCalculator.class);
//    priceDao = mock(PriceDao.class);
//    emaDao = mock(ValueDao.class);
//    priorValueProvider = mock(PriorValueProvider.class);
//
//    adjustPrices = new DummyProcess(calculator, priceDao, emaDao, priorValueProvider);
//  }
//
//  @Test
//  public void testProvideCalcParams_shouldGetPriorValues() {
//    final ArgumentCaptor<PriorValueRequest> requestCaptor = ArgumentCaptor.forClass(PriorValueRequest.class);
//    adjustPrices.provideCalcParams("MEG", TimeFrame.ONE_DAY,
//        Lists.newArrayList(
//            Price.builder().date(now).close(1).build(),
//            Price.builder().date(now.plusDays(1)).close(2).build()
//        ),
//        Lists.newArrayList(10, 50));
//
//    verify(priorValueProvider).providePriorValues(requestCaptor.capture());
//
//    final PriorValueRequest request = requestCaptor.getValue();
//
//    assertThat(request.getSymbol()).isEqualTo("MEG");
//    assertThat(request.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);
//    assertThat(request.getPriorDate()).isEqualTo(now);
//    assertThat(request.getPeriods()).containsExactly(10, 50);
//  }
//
//  @Test
//  public void testProvideCalcParams_shouldReturnCalcParams() {
//    final List<Price> prices = Lists.newArrayList(
//        Price.builder().date(now).close(1).build(),
//        Price.builder().date(now.plusDays(1)).close(2).build()
//    );
//    final Map<Integer, Value> result = Maps.newHashMap();
//    result.put(10, Value.of(now, 13.43));
//    result.put(50, Value.of(now, 14.32));
//
//    when(priorValueProvider.providePriorValues(any(PriorValueRequest.class))).thenReturn(result);
//
//    final CalcParams<Value> calcParams = adjustPrices.provideCalcParams("MEG", TimeFrame.ONE_DAY,
//        prices, Lists.newArrayList(10, 50));
//
//    assertThat(calcParams).isNotNull();
//    assertThat(calcParams.getPrices()).isEqualTo(prices);
//    assertThat(calcParams.getPeriods()).containsExactly(10, 50);
//    assertThat(calcParams.getPriorValueForPeriod(10)).hasValue(Value.of(now, 13.43));
//    assertThat(calcParams.getPriorValueForPeriod(50)).hasValue(Value.of(now, 14.32));
//  }
//
//  /**
//   * Dummy Test Class
//   */
//  public static final class DummyProcess extends AbstractCalcContinuedValuesProcess {
//
//    public DummyProcess(final ValueCalculator calculator,
//                      final PriceDao priceDao,
//                      final ValueDao valueDao,
//                      final PriorValueProvider priorValueProvider) {
//      super(calculator, priceDao, valueDao, priorValueProvider);
//    }
//
//    @Override
//    protected List<Integer> getDefaultPeriods() {
//      return Lists.newArrayList(10, 50, 100);
//    }
//
//    @Override
//    public String getProcessName() {
//      return "dummy";
//    }
//  }
//}
