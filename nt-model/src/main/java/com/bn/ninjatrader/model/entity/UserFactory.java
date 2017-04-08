package com.bn.ninjatrader.model.entity;

import com.bn.ninjatrader.model.util.IdGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class UserFactory {

  private final IdGenerator idGenerator;

  @Inject
  public UserFactory(final IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public User.Builder create() {
    return new User.Builder().userId(idGenerator.createId());
  }
}
