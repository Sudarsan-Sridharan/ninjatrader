package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.LocalProperties;
import com.bn.ninjatrader.simulation.model.Property;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class SetPropertyStatementTest {

  private final Variable var = Variable.of("DataType1");

  private final SetPropertyStatement orig = SetPropertyStatement.builder()
      .add("KEY1", var).add("KEY2", 100).add("KEY3", true).build();
  private final SetPropertyStatement equal = SetPropertyStatement.builder()
      .add("KEY1", var).add("KEY2", 100).add("KEY3", true).build();
  private final SetPropertyStatement diffProp1 = SetPropertyStatement.builder()
      .add("KEY1", Variable.of("DataType2")).add("KEY2", 100).add("KEY3", true).build();
  private final SetPropertyStatement diffProp2 = SetPropertyStatement.builder()
      .add("KEY1", var).add("KEY2", 100.01).add("KEY3", true).build();
  private final SetPropertyStatement diffProp3 = SetPropertyStatement.builder()
      .add("KEY1", var).add("KEY2", 100).add("KEY3", false).build();

  private World world;
  private BarData barData;
  private LocalProperties properties;

  @Before
  public void before() {
    world = mock(World.class);
    barData = mock(BarData.class);

    properties = new LocalProperties();

    when(world.getProperties()).thenReturn(properties);
    when(barData.get(var)).thenReturn(20d);
  }

  @Test
  public void testCreate_shouldSetStatementProperties() {
    assertThat(orig.getOperationProperties())
        .containsOnlyKeys("KEY1")
        .containsValue(var);
    assertThat(orig.getProperties())
        .containsOnlyKeys("KEY2", "KEY3")
        .containsValue(Property.of("KEY2", 100))
        .containsValue(Property.of("KEY3", true));
  }

  @Test
  public void testRun_shouldSetWorldProperties() {
    orig.run(world, barData);

    assertThat(properties).containsOnlyKeys("KEY1", "KEY2", "KEY3");
    assertThat(properties.get("KEY1").getValue()).isEqualTo(20d);
    assertThat(properties.get("KEY2").getValue()).isEqualTo(100d);
    assertThat(properties.get("KEY3").getValueAsBoolean()).isTrue();
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffProp1)
        .isNotEqualTo(diffProp2)
        .isNotEqualTo(diffProp3);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffProp1, diffProp2, diffProp3))
        .containsExactlyInAnyOrder(orig, diffProp1, diffProp2, diffProp3);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, SetPropertyStatement.class)).isEqualTo(orig);
  }
}
