package com.bn.ninjatrader.model.entity;

import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.common.type.Role;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class UserTest {

  @Test
  public void testCreateEmpty_shouldSetDefaults() {
    final User user = User.builder().build();
    assertThat(user.getRoles()).isEmpty();
    assertThat(user.getWatchList()).isEmpty();
  }

  @Test
  public void testCreateWithParams_shouldSetParamsToFields() {
    final User user = User.builder()
        .userId("ABC")
        .username("john.doe")
        .firstname("John")
        .lastname("Doe")
        .email("test@email.com")
        .mobile("0917888888")
        .addToWatchList("MEG")
        .addAllToWatchList(Lists.newArrayList("MEG", "BDO"))
        .addAllToWatchList("MEG", "TEL", "MRC")
        .addRole(Role.ADMIN)
        .addRoles(Lists.newArrayList(Role.ADMIN, Role.SUBSCRIBER))
        .addRoles(Role.ADMIN, Role.VIEWER)
        .build();

    assertThat(user.getUserId()).isEqualTo("ABC");
    assertThat(user.getUsername()).isEqualTo("john.doe");
    assertThat(user.getFirstname()).isEqualTo("John");
    assertThat(user.getLastname()).isEqualTo("Doe");
    assertThat(user.getEmail()).isEqualTo("test@email.com");
    assertThat(user.getMobile()).isEqualTo("0917888888");
    assertThat(user.getWatchList()).containsExactly("BDO", "MEG", "MRC", "TEL");
    assertThat(user.getRoles()).containsExactlyInAnyOrder(Role.ADMIN, Role.SUBSCRIBER, Role.VIEWER);
  }
}
