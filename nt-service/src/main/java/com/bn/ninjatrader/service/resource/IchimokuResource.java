//package com.bn.ninjatrader.service.resource;
//
//import com.bn.ninjatrader.model.deprecated.Ichimoku;
//import com.bn.ninjatrader.model.mongo.dao.IchimokuDao;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.bn.ninjatrader.service.model.IchimokuResponse;
//import com.bn.ninjatrader.service.model.PriceRequest;
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
//@Path("/ichimoku")
//@Produces(MediaType.APPLICATION_JSON)
//public class IchimokuResource extends AbstractDataResource {
//
//  private static final Logger LOG = LoggerFactory.getLogger(IchimokuResource.class);
//
//  private final IchimokuDao ichimokuService;
//
//  @Inject
//  public IchimokuResource(final IchimokuDao ichimokuService,
//                          final Clock clock) {
//    super(clock);
//    this.ichimokuService = ichimokuService;
//  }
//
//  @GET
//  @Path("/{symbol}")
//  public IchimokuResponse getIchimoku(@BeanParam final PriceRequest req) {
//    final FindRequest findRequest = req.toFindRequest(getClock());
//    final List<Ichimoku> ichimokuList = ichimokuService.find(findRequest);
//    return new IchimokuResponse().setValues(ichimokuList);
//  }
//}
