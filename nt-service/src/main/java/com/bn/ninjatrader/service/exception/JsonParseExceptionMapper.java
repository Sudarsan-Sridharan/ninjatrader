package com.bn.ninjatrader.service.exception;

import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
    private static final String ERROR_MSG = "Invalid JSON format. The request can not be parsed";

    @Override
    public Response toResponse(JsonParseException exception) {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(ERROR_MSG)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
