//package com.bn.ninjatrader.service.resource;
//
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.model.mongo.dao.EMADao;
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
//@Path("/ema")
//@Produces(MediaType.APPLICATION_JSON)
//public class EMAResource extends AbstractDataResource {
//  private static final Logger LOG = LoggerFactory.getLogger(EMAResource.class);
//
//  private final EMADao emaDao;
//
//  @Inject
//  public EMAResource(final EMADao emaDao,
//                     final Clock clock) {
//    super(clock);
//    this.emaDao = emaDao;
//  }
//
//  @GET
//  @Path("/{symbol}")
//  public MultiPeriodResponse<Value> getEMA(@BeanParam final MultiPeriodRequest req) {
//    final MultiPeriodResponse<Value> response = new MultiPeriodResponse<>();
//    for (final FindRequest findRequest : req.toFindRequest(getClock())) {
//      final List<Value> values = emaDao.find(findRequest);
//      response.put(findRequest.getPeriod(), values);
//    }
//    return response;
//  }
//}
