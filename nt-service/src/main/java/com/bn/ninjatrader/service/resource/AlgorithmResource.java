package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.model.CreateAlgorithmRequest;
import com.bn.ninjatrader.service.security.AuthenticatedUser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Secured
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
  public Response getAllTradeAlgorithm(final @Context SecurityContext securityContext) {
    final AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();
    final List<Algorithm> algos = algoDao.findAlgorithms().withUserId(user.getUserId()).now();
    return Response.ok(algos).build();
  }

  @GET
  @Path("/{algoId}")
  public Response getTradeAlgorithmById(@PathParam("algoId") final String algoId) {
    final Algorithm algo = algoDao.findOneByAlgorithmId(algoId)
        .orElseThrow(() -> new NotFoundException("algoId is not found."));
    return Response.ok(algo).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postTradeAlgorithm(final @Context SecurityContext securityContext,
                                     final CreateAlgorithmRequest req) {
    final AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();

    final Algorithm algo = Algorithm.builder()
        .algoId(req.getAlgoId())
        .userId(user.getUserId())
        .algorithm(req.getAlgorithm())
        .description(req.getDescription())
        .isAutoScan(req.isAutoScan())
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