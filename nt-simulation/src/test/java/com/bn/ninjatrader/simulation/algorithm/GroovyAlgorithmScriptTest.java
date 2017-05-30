package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.logic.Variable;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class GroovyAlgorithmScriptTest {

    @Test
    public void testCreateWithVariables_shouldParseAllVariablesInScript() {
        final String text = "def a = $SYMBOL; def b = $EMA100";
        final GroovyAlgorithmScript script = new GroovyAlgorithmScript(text);
        assertThat(script.getVariables())
            .containsExactlyInAnyOrder(
                Variable.of("$SYMBOL"),
                Variable.of("$EMA").withPeriod(100));
    }

    @Test
    public void testCreateWithHistoricalVariables_shouldParseVariablesAndIncludeBarsAgo() {
        final String text = "def a = $PRICE_LOW[6]; def b = $EMA20[100]";
        final GroovyAlgorithmScript script = new GroovyAlgorithmScript(text);

        assertThat(script.getScriptVariables())
            .containsExactlyInAnyOrder(
                ScriptVariable.of(Variable.of("$PRICE_LOW")).withBarsAgo(6),
                ScriptVariable.of(Variable.of("$EMA").withPeriod(20)).withBarsAgo(100));
        assertThat(script.getVariables())
            .containsExactlyInAnyOrder(
                Variable.of("$PRICE_LOW"),
                Variable.of("$EMA").withPeriod(20));
    }
}
