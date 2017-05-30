package com.bn.ninjatrader.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bn.ninjatrader.auth.config.AuthConfig;
import com.bn.ninjatrader.auth.exception.UserIdNotFoundException;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.entity.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TokenGenerator {
  private final Algorithm algorithm;
  private final UserDao userDao;
  private final IdGenerator idGenerator;
  private final Clock clock;
  private final AuthConfig config;

  @Inject
  public TokenGenerator(final Algorithm algorithm,
                        final UserDao userDao,
                        final IdGenerator idGenerator,
                        final Clock clock,
                        final AuthConfig config) {
    this.algorithm = algorithm;
    this.userDao = userDao;
    this.idGenerator = idGenerator;
    this.clock = clock;
    this.config = config;
  }

  public String createTokenForUserId(final String userId) {
    final User user = userDao.findByUserId(userId).orElseThrow(() -> new UserIdNotFoundException(userId));
    final Date expiry = Date.from(LocalDateTime.now(clock)
        .plusDays(config.getTokenExpiryDays())
        .atZone(ZoneId.of(config.getZoneId()))
        .toInstant());

    return JWT.create().withSubject(userId)
        .withJWTId(idGenerator.createId())
        .withClaim("fn", user.getFirstname())
        .withClaim("ln", user.getLastname())
        .withExpiresAt(expiry)
        .sign(algorithm);
  }
}
