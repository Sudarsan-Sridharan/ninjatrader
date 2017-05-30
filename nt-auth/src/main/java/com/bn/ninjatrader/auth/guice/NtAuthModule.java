package com.bn.ninjatrader.auth.guice;

import com.auth0.jwt.algorithms.Algorithm;
import com.bn.ninjatrader.auth.config.AuthConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.UnsupportedEncodingException;

/**
 * @author bradwee2000@gmail.com
 */
public class NtAuthModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  public Algorithm provideAlgo(final AuthConfig config) throws UnsupportedEncodingException {
    return Algorithm.HMAC256(config.getHsaKey());
  }
}
