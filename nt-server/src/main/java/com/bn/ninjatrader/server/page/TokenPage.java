package com.bn.ninjatrader.server.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Produces(MediaType.TEXT_HTML)
@Path("/token")
public class TokenPage {
  private static final Logger LOG = LoggerFactory.getLogger(TokenPage.class);
  private static final String TOKEN_COOKIE_NAME = "au";
  private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;
  private static final int COOKIE_VERSION = 1;

  @GET
  public Response saveTokenToCookie(@QueryParam("token") final String token) {
    final NewCookie cookie = new NewCookie(TOKEN_COOKIE_NAME,
        token,
        null,
        null,
        COOKIE_VERSION,
        null,
        COOKIE_MAX_AGE,
        false);

    return Response.ok().cookie(cookie).build();
  }
}
