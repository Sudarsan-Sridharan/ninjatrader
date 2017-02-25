//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.RSICalculator;
//import com.bn.ninjatrader.model.deprecated.RSIValue;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.mongo.dao.RSIDao;
//import com.bn.ninjatrader.process.provider.PriorValueProvider;
//import com.bn.ninjatrader.process.util.CalcProcessNames;
//import com.google.common.collect.Lists;
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by Brad on 6/8/16.
// */
//@Singleton
//public class CalcRSIProcess extends AbstractCalcContinuedValuesProcess<RSIValue> {
//  private static final Logger LOG = LoggerFactory.getLogger(CalcRSIProcess.class);
//  private static final List<Integer> DEFAULT_PERIODS = Collections.unmodifiableList(Lists.newArrayList(10, 14, 20));
//
//  @Inject
//  public CalcRSIProcess(final RSICalculator calculator,
//                        final PriceDao priceDao,
//                        final RSIDao rsiDao,
//                        final PriorValueProvider priorValueProvider) {
//    super(calculator, priceDao, rsiDao, priorValueProvider);
//  }
//
//  @Override
//  protected List<Integer> getDefaultPeriods() {
//    return DEFAULT_PERIODS;
//  }
//
//  @Override
//  public String getProcessName() {
//    return CalcProcessNames.RSI;
//  }
//}
