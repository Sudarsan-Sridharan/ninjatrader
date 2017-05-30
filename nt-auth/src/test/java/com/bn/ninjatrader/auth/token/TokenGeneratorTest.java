package com.bn.ninjatrader.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bn.ninjatrader.auth.config.AuthConfig;
import com.bn.ninjatrader.auth.exception.UserIdNotFoundException;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.entity.User;
import com.netflix.archaius.api.Config;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class TokenGeneratorTest {

  private LocalDateTime now = LocalDateTime.of(2017, 2, 1, 12, 30, 50);
  private Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();
  private Algorithm algorithm = Algorithm.HMAC256("testKey");
  private UserDao userDao;
  private IdGenerator idGenerator;
  private AuthConfig config;
  private Clock clock;
  private User user;

  private TokenGenerator tokenGenerator;

  public TokenGeneratorTest() throws UnsupportedEncodingException {
  }

  @Before
  public void before() {
    this.userDao = mock(UserDao.class);
    this.idGenerator = mock(IdGenerator.class);
    this.clock = Clock.fixed(nowInstant, ZoneId.systemDefault());
    this.config = new AuthConfig(mock(Config.class));
    this.user = User.builder().userId("XYZ").firstname("John").lastname("Doe").build();

    when(userDao.findByUserId(anyString())).thenReturn(Optional.of(user));
    when(idGenerator.createId()).thenReturn("RANDOM");

    this.tokenGenerator = new TokenGenerator(algorithm, userDao, idGenerator, clock, config);
  }

  @Test
  public void testCreateTokenForUserId_shouldGenerateValidToken() {
    final String token = tokenGenerator.createTokenForUserId("XYZ");

    assertThat(token).isNotNull();

    // Verify and decode token
    final JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm);
    final com.auth0.jwt.interfaces.Clock dateNow = () -> Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    final JWTVerifier verifier = verification.build(dateNow);
    final DecodedJWT decoded = verifier.verify(token);

    // Verify that claims are correct
    assertThat(decoded.getId()).isEqualTo("RANDOM");
    assertThat(decoded.getAlgorithm()).isEqualTo("HS256");
    assertThat(decoded.getSubject()).isEqualTo("XYZ");
    assertThat(decoded.getClaim("fn").asString()).isEqualTo("John");
    assertThat(decoded.getClaim("ln").asString()).isEqualTo("Doe");
  }

  @Test
  public void testCreateTokenForNotFoundUserId_shouldThrowException() {
    when(userDao.findByUserId(anyString())).thenReturn(Optional.empty());

    assertThatExceptionOfType(UserIdNotFoundException.class)
        .isThrownBy(() -> tokenGenerator.createTokenForUserId("XYZ"))
        .withMessageContaining("XYZ");
  }
}
