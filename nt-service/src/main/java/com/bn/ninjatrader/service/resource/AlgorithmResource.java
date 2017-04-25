package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.Algorithm;
import com.bn.ninjatrader.service.model.CreateAlgorithmRequest;
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
@Produces(MediaType.APPLICATION_JSON)
@Path("/algorithms")
public class AlgorithmResource {
  private static final Logger LOG = LoggerFactory.getLogger(AlgorithmResource.class);

  private final AlgorithmDao algoDao;

  @Inject
  public AlgorithmResource(final AlgorithmDao algoDao) {
    this.algoDao = algoDao;
  }

  @GET
  public Response getAllTradeAlgorithm() {
    // TODO get userId from jwt from auth header
    final List<Algorithm> algos = algoDao.findByUserId("ADMIN");
    return Response.ok(algos).build();
  }

  @GET
  @Path("/{algoId}")
  public Response getTradeAlgorithmById(@PathParam("algoId") final String algoId) {
    final Algorithm algo = algoDao.findByAlgorithmId(algoId)
        .orElseThrow(() -> new NotFoundException("algoId is not found."));
    return Response.ok(algo).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postTradeAlgorithm(final CreateAlgorithmRequest req) {
    final Algorithm algo = Algorithm.builder()
        .algoId(req.getAlgoId())
        .userId("ADMIN") // TODO get userId from jwt from auth header
        .algorithm(req.getAlgorithm())
        .description(req.getDescription())
        .build();

    final Algorithm savedAlgorithm = algoDao.save(algo);

    return Response.ok(savedAlgorithm).build();
  }

  @DELETE
  @Path("/{algoId}")
  public Response deleteTradeAlgorithm(@PathParam("algoId") final String algoId) {
    algoDao.delete(algoId);
    return Response.ok().build();
  }
}