package com.bn.ninjatrader.service.exception;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author bradwee2000@gmail.com
 */
public class Exceptions {

    private Exceptions() {}

    public static void throwBadRequest(final String message) {
        throw new BadRequestException(message, Response.status(BAD_REQUEST).entity(message).build());
    }
}
