package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
public class ScriptVariable implements Serializable {

    private static final String HISTORY_NAME_FORMAT = "%s[%s]";
    private static final String HISTORY_SAFE_NAME_FORMAT = "%s_$BARSAGO%s";

    public static final ScriptVariable of(final Variable variable) {
        return new ScriptVariable(variable, 0);
    }

    private final Variable variable;
    private final int barsAgo;
    private final String name;
    private final String safeName;

    private ScriptVariable() {
        this.variable = null;
        this.barsAgo = 0;
        this.name = null;
        this.safeName = null;
    }

    public ScriptVariable(final Variable variable, final int barsAgo) {
        this.variable = variable;
        this.barsAgo = barsAgo;
        this.name = barsAgo == 0 ?
            variable.getName() :
            String.format(HISTORY_NAME_FORMAT, variable.getName(), barsAgo);
        this.safeName = barsAgo == 0 ?
            variable.getName() :
            String.format(HISTORY_SAFE_NAME_FORMAT, variable.getName(), barsAgo);
    }

    public Variable getVariable() {
        return variable;
    }

    public int getBarsAgo() {
        return barsAgo;
    }

    public String getName() {
        return name;
    }

    public String getSafeName() {
        return safeName;
    }

    public ScriptVariable withBarsAgo(final int barsAgo) {
        return new ScriptVariable(this.variable, barsAgo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptVariable that = (ScriptVariable) o;
        return barsAgo == that.barsAgo &&
            Objects.equal(variable, that.variable) &&
            Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(variable, barsAgo, name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("variable", variable)
            .add("barsAgo", barsAgo)
            .add("name", name)
            .toString();
    }
}
