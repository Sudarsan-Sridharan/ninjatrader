package com.bn.ninjatrader.auth.token;

import com.google.common.collect.Lists;
import org.junit.Test;

import static com.bn.ninjatrader.common.type.Role.ADMIN;
import static com.bn.ninjatrader.common.type.Role.SUBSCRIBER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class DecodedTokenTest {

  @Test
  public void testBuild_shouldSetProperties() {
    final DecodedToken token = DecodedToken.builder()
        .tokenId("abc").userId("zzz").firstName("John").lastName("Doe")
        .addRole(ADMIN)
        .addRoleIds(Lists.newArrayList(SUBSCRIBER.getId()))
        .build();

    assertThat(token.getTokenId()).isEqualTo("abc");
    assertThat(token.getUserId()).isEqualTo("zzz");
    assertThat(token.getFirstName()).isEqualTo("John");
    assertThat(token.getLastName()).isEqualTo("Doe");
    assertThat(token.getRoles()).containsExactlyInAnyOrder(ADMIN, SUBSCRIBER);
  }

  @Test
  public void testHasRole_shouldReturnTrueIfRoleExists() {
    final DecodedToken token = DecodedToken.builder()
        .tokenId("abc").userId("zzz").firstName("John").lastName("Doe")
        .addRoleIds(Lists.newArrayList("admin", "subscriber"))
        .build();

    assertThat(token.hasRole(ADMIN.getId())).isTrue();
    assertThat(token.hasRole(ADMIN)).isTrue();

    assertThat(token.hasRole(SUBSCRIBER.getId())).isTrue();
    assertThat(token.hasRole(SUBSCRIBER)).isTrue();

    assertThat(token.hasRole("UNKNOWN_ROLE")).isFalse();
  }
}
