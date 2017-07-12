package com.bn.ninjatrader.service.exception;

import com.bn.ninjatrader.auth.exception.UnauthorizedMethodAccessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
public class UnauthorizedMethodAccessExceptionMapper implements ExceptionMapper<UnauthorizedMethodAccessException> {
    private static final String ERROR_MSG = "You do not have enough permissions to access resource.";

    @Override
    public Response toResponse(final UnauthorizedMethodAccessException exception) {
        return Response
            .status(Response.Status.UNAUTHORIZED)
            .entity(ERROR_MSG)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
