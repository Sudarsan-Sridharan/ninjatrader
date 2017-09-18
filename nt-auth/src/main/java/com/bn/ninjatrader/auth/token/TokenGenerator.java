package com.bn.ninjatrader.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bn.ninjatrader.auth.config.AuthConfig;
import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.common.util.IdGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

import static com.bn.ninjatrader.auth.attribute.TokenAttrNames.FIRST_NAME;
import static com.bn.ninjatrader.auth.attribute.TokenAttrNames.LAST_NAME;
import static com.bn.ninjatrader.auth.attribute.TokenAttrNames.ROLES;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TokenGenerator {
  private final Algorithm algorithm;
  private final IdGenerator idGenerator;
  private final Clock clock;
  private final AuthConfig config;

  @Inject
  public TokenGenerator(final Algorithm algorithm,
                        final IdGenerator idGenerator,
                        final Clock clock,
                        final AuthConfig config) {
    this.algorithm = algorithm;
    this.idGenerator = idGenerator;
    this.clock = clock;
    this.config = config;
  }

  public String createTokenForUser(final User user) {
    final Date expiry = Date.from(LocalDateTime.now(clock)
        .plusDays(config.getTokenExpiryDays())
        .atOffset(ZoneOffset.UTC)
        .toInstant());

    return JWT.create().withSubject(user.getUserId())
        .withJWTId(idGenerator.createId())
        .withClaim(FIRST_NAME, user.getFirstname())
        .withClaim(LAST_NAME, user.getLastname())
        .withArrayClaim(ROLES, user.getRoles()
            .stream()
            .map(r -> r.getId())
            .collect(Collectors.toList())
            .toArray(new String [] {}))
//        .withExpiresAt(expiry)
        .sign(algorithm);
  }

  public String signPayload(final String payload, final String secretKey) {
    final Date expiry = Date.from(LocalDateTime.now(clock)
        .plusMinutes(1)
        .atOffset(ZoneOffset.UTC)
        .toInstant());

    try {
      return JWT.create()
          .withClaim("d", payload)
          .withArrayClaim(ROLES, new String[] { Role.SYSTEM.getId() })
          .withExpiresAt(expiry)
          .sign(Algorithm.HMAC256(secretKey));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
