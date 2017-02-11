package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.Marker;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class MarkStatementTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);

  private final MarkStatement orig = MarkStatement.withColor("yellow");
  private final MarkStatement equal = MarkStatement.withColor("yellow");
  private final MarkStatement diffColor = MarkStatement.withColor("red");
  private final MarkStatement diffNumOfBarsAgo = MarkStatement.withColor("yellow").numOfBarsAgo(5);
  private final MarkStatement diffMarker = MarkStatement.withColor("yellow").marker(Marker.ARROW_BOTTOM);

  private World world;
  private History history;
  private Price price;
  private BarData barData;
  private List<Mark> marks;

  @Before
  public void before() {
    world = mock(World.class);
    history = mock(History.class);
    price = mock(Price.class);
    barData = mock(BarData.class);

    marks = Lists.newArrayList();

    when(barData.getWorld()).thenReturn(world);
    when(barData.getPrice()).thenReturn(price);
    when(price.getDate()).thenReturn(now);
    when(world.getChartMarks()).thenReturn(marks);
    when(world.getHistory()).thenReturn(history);
    when(history.getNBarsAgo(anyInt())).thenReturn(Optional.of(barData));
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getColor()).isEqualTo("yellow");
    assertThat(orig.getNumOfBarsAgo()).isEqualTo(0);
    assertThat(orig.getMarker()).isEqualTo(Marker.ARROW_TOP);
    assertThat(diffColor.getColor()).isEqualTo("red");
    assertThat(diffNumOfBarsAgo.getNumOfBarsAgo()).isEqualTo(5);
    assertThat(diffMarker.getMarker()).isEqualTo(Marker.ARROW_BOTTOM);
  }

  @Test
  public void testRun_shouldAddMarkOnTheDate() {
    orig.run(barData);

    assertThat(marks).containsExactly(Mark.onDate(now).withColor("yellow").withMarker(Marker.ARROW_TOP));
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffColor)
        .isNotEqualTo(diffNumOfBarsAgo)
        .isNotEqualTo(diffMarker);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffColor, diffNumOfBarsAgo, diffMarker))
        .containsExactlyInAnyOrder(orig, diffColor, diffNumOfBarsAgo, diffMarker);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Statement.class)).isEqualTo(orig);
  }
}
