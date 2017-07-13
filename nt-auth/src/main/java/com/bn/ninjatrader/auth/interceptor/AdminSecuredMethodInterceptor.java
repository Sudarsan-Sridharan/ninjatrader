package com.bn.ninjatrader.auth.interceptor;

import com.bn.ninjatrader.auth.exception.InvalidArgumentException;
import com.bn.ninjatrader.auth.exception.UnauthorizedMethodAccessException;
import com.bn.ninjatrader.auth.token.DecodedToken;
import com.bn.ninjatrader.auth.token.JwtTokenVerifier;
import com.bn.ninjatrader.common.type.Role;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AdminSecuredMethodInterceptor implements MethodInterceptor {
  private static final Logger LOG = LoggerFactory.getLogger(AdminSecuredMethodInterceptor.class);
  private static final String AUTH_HEADER = "Authorization";
  private static final String WHITESPACE = " ";

  private final Provider<JwtTokenVerifier> tokenVerifier;

  public AdminSecuredMethodInterceptor(final Provider<JwtTokenVerifier> tokenVerifier) {
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    if (invocation.getArguments().length == 0
        || !(invocation.getArguments()[0] instanceof HttpServletRequest)) {
      throw new InvalidArgumentException();
    }

    final HttpServletRequest req = (HttpServletRequest) invocation.getArguments()[0];

    verifyRole(req);

    return invocation.proceed();
  }

  private void verifyRole(final HttpServletRequest req) {
    final String authentication = req.getHeader(AUTH_HEADER);

    if (StringUtils.isEmpty(authentication) || !authentication.contains(WHITESPACE)) {
      throw new UnauthorizedMethodAccessException();
    }

    final String[] split = authentication.split(WHITESPACE);
    final String token = split[1];

    final DecodedToken decodedToken = tokenVerifier.get().verifyToken(token);

    if (!decodedToken.getRoles().contains(Role.ADMIN.getId())) {
      throw new UnauthorizedMethodAccessException();
    }
  }
}
