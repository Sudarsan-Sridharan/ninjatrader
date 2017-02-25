//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.ValueCalculator;
//import com.bn.ninjatrader.calculator.parameter.CalcParams;
//import com.bn.ninjatrader.common.data.Price;
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.common.type.TimeFrame;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.mongo.dao.ValueDao;
//import com.bn.ninjatrader.process.provider.PriorValueProvider;
//import com.bn.ninjatrader.process.provider.PriorValueRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//
//import static com.google.common.base.Preconditions.checkArgument;
//import static com.google.common.base.Preconditions.checkNotNull;
//
///**
// * @author bradwee2000@gmail.com
// */
//public abstract class AbstractCalcContinuedValuesProcess<T extends Value> extends AbstractCalcValuesProcess {
//  private static final Logger LOG = LoggerFactory.getLogger(AbstractCalcContinuedValuesProcess.class);
//
//  private final PriorValueProvider priorValueProvider;
//  private final ValueDao valueDao;
//
//  public AbstractCalcContinuedValuesProcess(final ValueCalculator calculator,
//                                            final PriceDao priceDao,
//                                            final ValueDao valueDao,
//                                            final PriorValueProvider priorValueProvider) {
//    super(calculator, priceDao, valueDao);
//    this.valueDao = valueDao;
//    this.priorValueProvider = priorValueProvider;
//  }
//
//  @Override
//  public CalcParams<T> provideCalcParams(final String symbol,
//                                         final TimeFrame timeFrame,
//                                         final List<Price> prices,
//                                         final List<Integer> periods) {
//    checkNotNull(prices, "prices must not be null.");
//    checkArgument(!prices.isEmpty(), "prices must not be empty.");
//
//    final PriorValueRequest priorValueRequest = PriorValueRequest.builder()
//        .dao(valueDao)
//        .symbol(symbol)
//        .timeFrame(timeFrame)
//        .priorDate(prices.get(0).getDate())
//        .addPeriods(periods)
//        .build();
//
//    final Map<Integer, T> priorValues = priorValueProvider.providePriorValues(priorValueRequest);
//
//    return CalcParams.withPrices(prices).periods(periods).addAllPriorValues(priorValues);
//  }
//}
