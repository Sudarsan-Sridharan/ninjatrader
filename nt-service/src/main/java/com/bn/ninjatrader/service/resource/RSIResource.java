//package com.bn.ninjatrader.service.resource;
//
//import com.bn.ninjatrader.model.deprecated.RSIValue;
//import com.bn.ninjatrader.model.mongo.dao.RSIDao;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.bn.ninjatrader.service.model.MultiPeriodRequest;
//import com.bn.ninjatrader.service.model.MultiPeriodResponse;
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.ws.rs.BeanParam;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import java.time.Clock;
//import java.util.List;
//
///**
// * @author bradwee2000@gmail.com
// */
//@Singleton
//@Path("/rsi")
//@Produces(MediaType.APPLICATION_JSON)
//public class RSIResource extends AbstractDataResource {
//  private static final Logger LOG = LoggerFactory.getLogger(RSIResource.class);
//
//  private final RSIDao rsiDao;
//
//  @Inject
//  public RSIResource(final RSIDao rsiDao,
//                     final Clock clock) {
//    super(clock);
//    this.rsiDao = rsiDao;
//  }
//
//  @GET
//  @Path("/{symbol}")
//  public MultiPeriodResponse<RSIValue> getRSI(@BeanParam final MultiPeriodRequest req) {
//    final MultiPeriodResponse<RSIValue> response = new MultiPeriodResponse();
//    for (final FindRequest findRequest : req.toFindRequest(getClock())) {
//      final List<RSIValue> values = rsiDao.find(findRequest);
//      response.put(findRequest.getPeriod(), values);
//    }
//    return response;
//  }
//}
