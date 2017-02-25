//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.EMACalculator;
//import com.bn.ninjatrader.model.mongo.dao.EMADao;
//import com.bn.ninjatrader.model.dao.PriceDao;
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
//public class CalcEMAProcess extends AbstractCalcContinuedValuesProcess {
//
//  private static final Logger LOG = LoggerFactory.getLogger(CalcEMAProcess.class);
//  private static final List<Integer> DEFAULT_PERIODS =
//      Collections.unmodifiableList(Lists.newArrayList(18, 50, 100, 200));
//
//  @Inject
//  public CalcEMAProcess(final EMACalculator calculator,
//                        final PriceDao priceDao,
//                        final EMADao emaDao,
//                        final PriorValueProvider priorValueProvider) {
//    super(calculator, priceDao, emaDao, priorValueProvider);
//  }
//
//  @Override
//  protected List<Integer> getDefaultPeriods() {
//    return DEFAULT_PERIODS;
//  }
//
//  @Override
//  public String getProcessName() {
//    return CalcProcessNames.EMA;
//  }
//}
