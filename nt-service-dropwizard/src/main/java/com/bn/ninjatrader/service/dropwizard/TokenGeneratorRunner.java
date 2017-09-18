package com.bn.ninjatrader.service.dropwizard;

import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.auth.token.TokenGenerator;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.archaius.guice.ArchaiusModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class TokenGeneratorRunner {

  private static final Logger LOG = LoggerFactory.getLogger(TokenGeneratorRunner.class);

  public static void main(String args[]) {
    final Injector injector = Guice.createInjector(
        new ArchaiusModule(),
        new NtAuthModule(),
        new NtModelMongoModule());
    final TokenGenerator generator = injector.getInstance(TokenGenerator.class);

    String token = generator.createTokenForUser(User.builder().userId("ADMIN").addRole(Role.ADMIN).build());
    LOG.info("Admin TOKEN: {}", token);
    token = generator.createTokenForUser(User.builder().userId("TINO").addRole(Role.SUBSCRIBER).build());
    LOG.info("Tino TOKEN: {}", token);
//    final String token = generator.createTokenForUser(User.builder().addRole(Role.SYSTEM).build());

  }
}
