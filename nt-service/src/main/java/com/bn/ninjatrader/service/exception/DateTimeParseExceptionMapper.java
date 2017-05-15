package com.bn.ninjatrader.service.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.format.DateTimeParseException;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
public class DateTimeParseExceptionMapper implements ExceptionMapper<DateTimeParseException> {
  private static final String ERROR_MSG = "Invalid date format.";

  @Override
  public Response toResponse(final DateTimeParseException exception) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(ERROR_MSG)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
