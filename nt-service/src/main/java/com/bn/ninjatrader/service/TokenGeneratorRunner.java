package com.bn.ninjatrader.service;

import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.auth.token.TokenGenerator;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.model.entity.User;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class TokenGeneratorRunner {

  private static final Logger LOG = LoggerFactory.getLogger(TokenGeneratorRunner.class);

  public static void main(String args[]) {
    final Injector injector = Guice.createInjector(
        new NtAuthModule(),
        new NtModelMongoModule());
    final TokenGenerator generator = injector.getInstance(TokenGenerator.class);

    final String token = generator.createTokenForUser(User.builder().userId("test").addRole(Role.ADMIN).build());
    LOG.info("TOKEN: {}", token);
  }
}
