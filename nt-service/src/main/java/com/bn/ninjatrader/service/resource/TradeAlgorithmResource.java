package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.entity.TradeAlgorithmFactory;
import com.bn.ninjatrader.service.model.CreateTradeAlgorithmRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/algos")
public class TradeAlgorithmResource {
  private static final Logger LOG = LoggerFactory.getLogger(TradeAlgorithmResource.class);

  private final TradeAlgorithmDao algoDao;
  private final TradeAlgorithmFactory algoFactory;

  @Inject
  public TradeAlgorithmResource(final TradeAlgorithmDao algoDao, final TradeAlgorithmFactory algoFactory) {
    this.algoDao = algoDao;
    this.algoFactory = algoFactory;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllTradeAlgorithm() {

    // TODO get userId from jwt from auth header
    final List<TradeAlgorithm> algos = algoDao.findByUserId("ADMIN");
    return Response.ok(algos).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{algoId}")
  public Response getTradeAlgorithmById(@PathParam("algoId") final String algoId) {
    final TradeAlgorithm algo = algoDao.findByTradeAlgorithmId(algoId)
        .orElseThrow(() -> new NotFoundException("algoId is not found."));
    return Response.ok(algo).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postTradeAlgorithm(final CreateTradeAlgorithmRequest req) {
    final TradeAlgorithm algo = algoFactory.create()
        .userId("ADMIN") // TODO get userId from jwt from auth header
        .algorithm(req.getAlgorithm())
        .description(req.getDescription())
        .build();

    algoDao.save(algo);

    return Response.ok(algo).build();
  }
}