//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.MeanCalculator;
//import com.bn.ninjatrader.model.mongo.dao.MeanDao;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.adjustPrices.util.CalcProcessNames;
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
//public class CalcMeanProcess extends AbstractCalcValuesProcess {
//
//  private static final Logger LOG = LoggerFactory.getLogger(CalcMeanProcess.class);
//  private static final List<Integer> DEFAULT_PERIODS = Collections.unmodifiableList(Lists.newArrayList(9, 26, 52));
//
//  @Inject
//  public CalcMeanProcess(final MeanCalculator calculator,
//                         final PriceDao priceDao,
//                         final MeanDao meanDao) {
//    super(calculator, priceDao, meanDao);
//  }
//
//  @Override
//  protected List<Integer> getDefaultPeriods() {
//    return DEFAULT_PERIODS;
//  }
//
//  @Override
//  public String getProcessName() {
//    return CalcProcessNames.MEAN;
//  }
//}
