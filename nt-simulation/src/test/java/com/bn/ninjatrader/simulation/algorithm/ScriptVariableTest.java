package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.logic.Variable;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class ScriptVariableTest {

    final Variable variable = Variable.of("Test").withPeriod(20);

    @Test
    public void testCreate_shouldSetProperties() {
        final ScriptVariable scriptVariable = ScriptVariable.of(variable);

        assertThat(scriptVariable.getVariable()).isEqualTo(variable);
        assertThat(scriptVariable.getName()).isEqualTo("Test20");
    }

    @Test
    public void testCreateWithBarsAgo_shouldSetBarsAgo() {
        final ScriptVariable scriptVariable = ScriptVariable.of(variable).withBarsAgo(6);

        assertThat(scriptVariable.getVariable()).isEqualTo(variable);
        assertThat(scriptVariable.getBarsAgo()).isEqualTo(6);
        assertThat(scriptVariable.getName()).isEqualTo("Test20[6]");
    }
}
