//package com.bn.ninjatrader.process.provider;
//
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.model.mongo.dao.ValueDao;
//import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
//import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author bradwee2000@gmail.com
// */
//@Singleton
//public class PriorValueProvider {
//  private static final Logger LOG = LoggerFactory.getLogger(PriorValueProvider.class);
//
//  public <T extends Value> Map<Integer, T> providePriorValues(final PriorValueRequest request) {
//    final Map<Integer, T> result = Maps.newHashMap();
//    final ValueDao<T> dao = request.getValueDao();
//
//    // For each period, get the prior value to continue calculating from.
//    for (final int period : request.getPeriods()) {
//
//      // Find prior value
//      final List<T> values = dao.findBeforeDate(FindBeforeDateRequest.builder()
//          .symbol(request.getSymbol())
//          .timeFrame(request.getTimeFrame())
//          .numOfValues(1)
//          .beforeDate(request.getPriorDate())
//          .period(period)
//          .build());
//
//      if (!values.isEmpty()) {
//        result.put(period, values.get(0));
//      }
//    }
//    return result;
//  }
//}
