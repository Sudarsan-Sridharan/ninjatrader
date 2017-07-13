package com.bn.ninjatrader.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bn.ninjatrader.auth.exception.ExpiredTokenException;
import com.bn.ninjatrader.auth.exception.InvalidTokenException;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bradwee2000@gmail.com
 */
public class TokenVerifierTest {

  private Algorithm algorithm = Algorithm.HMAC256("testKey");

  private JwtTokenVerifier tokenVerifier;

  public TokenVerifierTest() throws UnsupportedEncodingException {
  }

  @Before
  public void before() {
    this.tokenVerifier = new JwtTokenVerifier(algorithm);
  }

  @Test
  public void testVerifyValidToken_shouldReturnDecodedToken() {
    final Date expiry = Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant());
    final String signedToken = JWT.create()
        .withJWTId("abc").withSubject("123")
        .withClaim("fn", "John")
        .withClaim("ln", "Doe")
        .withExpiresAt(expiry)
        .sign(algorithm);

    final DecodedToken token = tokenVerifier.verifyToken(signedToken);

    assertThat(token).isNotNull();
    assertThat(token.getTokenId()).isEqualTo("abc");
    assertThat(token.getUserId()).isEqualTo("123");
    assertThat(token.getFirstName()).isEqualTo("John");
    assertThat(token.getLastName()).isEqualTo("Doe");
  }

  @Test
  public void testVerifyExpiredToken_shouldThrowException() {
    final Date expiry = Date.from(LocalDateTime.now().minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant());
    final String signedToken = JWT.create()
        .withJWTId("abc").withSubject("123").withExpiresAt(expiry).sign(algorithm);

    assertThatThrownBy(() -> tokenVerifier.verifyToken(signedToken)).isExactlyInstanceOf(ExpiredTokenException.class);
  }

  @Test
  public void testVerifyUnknownToken_shouldThrowException() {
    assertThatThrownBy(() -> tokenVerifier.verifyToken("INVALID")).isExactlyInstanceOf(InvalidTokenException.class);
  }

  @Test
  public void testVerifyTokenInvalidSignKey_shouldThrowException() throws UnsupportedEncodingException {
    final String signedToken = JWT.create()
        .withJWTId("abc").withSubject("123").sign(Algorithm.HMAC256("invalidKey"));
    assertThatThrownBy(() -> tokenVerifier.verifyToken(signedToken)).isExactlyInstanceOf(InvalidTokenException.class);
  }
}
