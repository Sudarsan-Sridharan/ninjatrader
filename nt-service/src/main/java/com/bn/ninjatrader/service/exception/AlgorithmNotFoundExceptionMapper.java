package com.bn.ninjatrader.service.exception;

import com.bn.ninjatrader.simulation.exception.AlgorithmNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
public class AlgorithmNotFoundExceptionMapper implements ExceptionMapper<AlgorithmNotFoundException> {
  private static final String ERROR_MSG = "Algorithm not found.";

  @Override
  public Response toResponse(final AlgorithmNotFoundException e) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(ERROR_MSG)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
