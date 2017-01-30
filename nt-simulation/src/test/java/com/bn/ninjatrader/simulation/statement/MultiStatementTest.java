package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author bradwee2000@gmail.com
 */
public class MultiStatementTest {

  private final MockStatement mockStatement1 = MockStatement.newInstance().withVariables(Variable.of("DataType1"));
  private final MockStatement mockStatement2 = MockStatement.newInstance().withVariables(Variable.of("DataType2"));
  private final MultiStatement orig = MultiStatement.builder().add(mockStatement1).build();
  private final MultiStatement equal = MultiStatement.builder().add(mockStatement1).build();
  private final MultiStatement diffStatement = MultiStatement.builder().add(mockStatement2).build();
  private final MultiStatement multiStatements = MultiStatement.builder().addAll(mockStatement1, mockStatement2).build();

  private World world;
  private BarData barData;

  @Before
  public void before() {
    world = mock(World.class);
    barData = mock(BarData.class);
    mockStatement1.reset();
    mockStatement2.reset();
  }

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getStatementList()).containsExactly(mockStatement1);
    assertThat(multiStatements.getStatementList()).containsExactly(mockStatement1, mockStatement2);
  }

  @Test
  public void testRun_shouldRunAllStatements() {
    final Statement multi = MultiStatement.builder().addAll(mockStatement1, mockStatement2).build();

    multi.run(barData);

    assertThat(mockStatement1.isRun()).isTrue();
    assertThat(mockStatement2.isRun()).isTrue();
  }

  @Test
  public void testGetVariables_shouldReturnVariablesOfAllStatements() {
    final Statement multi = MultiStatement.builder().addAll(mockStatement1, mockStatement2).build();
    assertThat(multi.getVariables())
        .containsExactlyInAnyOrder(Variable.of("DataType1"), Variable.of("DataType2"));
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffStatement)
        .isNotEqualTo(multiStatements);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffStatement, multiStatements))
        .containsExactlyInAnyOrder(orig, diffStatement, multiStatements);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final MultiStatement statement = MultiStatement.builder().add(EmptyStatement.instance()).build();
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(statement);
    assertThat(om.readValue(json, Statement.class)).isEqualTo(statement);
  }
}
