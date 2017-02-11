package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class MarkTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final Mark orig = Mark.onDate(now).withColor("red");
  private final Mark equal = Mark.onDate(now).withColor("red");
  private final Mark diffDate = Mark.onDate(now.plusDays(1)).withColor("red");
  private final Mark diffColor = Mark.onDate(now).withColor("blue");
  private final Mark diffMarker = Mark.onDate(now).withColor("red").withMarker(Marker.ARROW_BOTTOM);

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getDate()).isEqualTo(now);
    assertThat(orig.getColor()).isEqualTo("red");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffColor)
        .isNotEqualTo(diffDate)
        .isNotEqualTo(diffMarker);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffDate, diffColor, diffMarker))
        .containsExactlyInAnyOrder(orig, diffDate, diffColor, diffMarker);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Mark.class)).isEqualTo(orig);
  }
}
