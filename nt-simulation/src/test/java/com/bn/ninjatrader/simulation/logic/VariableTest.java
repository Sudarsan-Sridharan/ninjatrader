package com.bn.ninjatrader.simulation.logic;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class VariableTest {

    @Test
    public void testCreate_shouldSetProperties() {
        final Variable variable = Variable.of("test");

        assertThat(variable.getName()).isEqualTo("test");
        assertThat(variable.getDataType()).isEqualTo("test");
        assertThat(variable.getPeriod()).isEqualTo(0);

        final Variable periodVariable = variable.withPeriod(10);
        assertThat(periodVariable.getName()).isEqualTo("test10");
        assertThat(periodVariable.getPeriod()).isEqualTo(10);
    }
}
