package com.bn.ninjatrader.auth.token;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class DecodedTokenTest {

  @Test
  public void testBuild_shouldSetProperties() {
    final DecodedToken token = DecodedToken.builder()
        .tokenId("abc").userId("zzz").firstName("John").lastName("Doe")
        .roles(Lists.newArrayList("Admin", "Subscriber"))
        .build();

    assertThat(token.getTokenId()).isEqualTo("abc");
    assertThat(token.getUserId()).isEqualTo("zzz");
    assertThat(token.getFirstName()).isEqualTo("John");
    assertThat(token.getLastName()).isEqualTo("Doe");
    assertThat(token.getRoles()).containsExactlyInAnyOrder("Admin", "Subscriber");
  }
}
