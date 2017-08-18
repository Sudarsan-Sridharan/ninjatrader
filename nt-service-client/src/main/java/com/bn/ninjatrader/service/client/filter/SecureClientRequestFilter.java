package com.bn.ninjatrader.service.client.filter;

import com.bn.ninjatrader.auth.token.TokenGenerator;
import com.bn.ninjatrader.service.client.annotation.ApiSecretKeyProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.archaius.api.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author bradwee2000@gmail.com
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecureClientRequestFilter implements ClientRequestFilter {
  private static final Logger LOG = LoggerFactory.getLogger(SecureClientRequestFilter.class);

  private final TokenGenerator tokenGenerator;
  private final ObjectMapper om;
  private final Property<String> secretKey;

  @Inject
  public SecureClientRequestFilter(final TokenGenerator tokenGenerator,
                                   final ObjectMapper om,
                                   @ApiSecretKeyProperty final Property<String> secretKey) {
    this.tokenGenerator = tokenGenerator;
    this.om = om;
    this.secretKey = secretKey;
  }

  @Override
  public void filter(ClientRequestContext ctx) throws IOException {
    final String json = ctx.getEntity() == null ? "{}" : om.writeValueAsString(ctx.getEntity());
    final String signed = tokenGenerator.signPayload(json, secretKey.get());
    ctx.getHeaders().add("Authorization", "Bearer " + signed);
  }
}
