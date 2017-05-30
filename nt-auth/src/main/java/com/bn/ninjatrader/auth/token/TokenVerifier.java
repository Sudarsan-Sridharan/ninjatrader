package com.bn.ninjatrader.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bn.ninjatrader.auth.exception.ExpiredTokenException;
import com.bn.ninjatrader.auth.exception.InvalidTokenException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TokenVerifier {

  private final JWTVerifier verifier;

  @Inject
  public TokenVerifier(final Algorithm algorithm) {
    this.verifier = JWT.require(algorithm).build();
  }

  public DecodedToken verifyToken(final String token) {
    try {
      final DecodedJWT jwt = verifier.verify(token);
      return DecodedToken.builder()
          .tokenId(jwt.getId())
          .userId(jwt.getSubject())
          .firstName(jwt.getClaim("fn").asString())
          .lastName(jwt.getClaim("ln").asString())
          .build();
    } catch (final TokenExpiredException e) {
      throw new ExpiredTokenException(e);
    } catch (final JWTVerificationException e) {
      throw new InvalidTokenException(e);
    }
  }
}
