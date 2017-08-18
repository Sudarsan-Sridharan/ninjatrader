package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.model.User;

import java.util.Optional;

/**
 * Created by Brad on 4/30/16.
 */
public interface UserDao {

  void saveUser(final User user);

  Optional<User> findByUserId(final String userId);

  Optional<User> findByUsername(final String userId);
}
