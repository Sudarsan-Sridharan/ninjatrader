package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.LocalProperties;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetPropertyStatement implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(SetPropertyStatement.class);

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("properties")
  private final LocalProperties properties = new LocalProperties();

  @JsonProperty("opProperties")
  private final Map<String, Operation<BarData>> opProperties = Maps.newHashMap();

  public SetPropertyStatement(@JsonProperty("opProperties") final Map<String, Operation<BarData>> opProperties,
                              @JsonProperty("properties") final LocalProperties properties) {
    checkNotNull(properties, "Properties must not be null.");
    this.opProperties.putAll(opProperties);
    this.properties.putAll(properties);
  }

  @Override
  public void run(final World world, final BarData barData) {
    final LocalProperties properties = barData.getWorld().getProperties();
    properties.putAll(this.properties);
    for (Map.Entry<String, Operation<BarData>> entry : opProperties.entrySet()) {
      final Operation<BarData> operation = entry.getValue();
      world.getProperties().put(entry.getKey(), operation.getValue(barData));
    }
  }

  public LocalProperties getProperties() {
    return new LocalProperties().putAll(properties);
  }

  public Map<String, Operation<BarData>> getOperationProperties() {
    return Maps.newHashMap(opProperties);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> set = Sets.newHashSet();
    for (final Operation operation : opProperties.values()) {
      set.addAll(operation.getVariables());
    }
    return set;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof SetPropertyStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final SetPropertyStatement rhs = (SetPropertyStatement) obj;
    return Objects.equal(properties, rhs.properties)
        && Objects.equal(opProperties, rhs.opProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(properties, opProperties);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("properties", properties).add("opProperties", opProperties).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private final Map<String, Operation<BarData>> opProperties = Maps.newHashMap();
    private final LocalProperties properties = new LocalProperties();

    public Builder add(final String key, final Operation<BarData> operation) {
      opProperties.put(key, operation);
      return this;
    }

    public Builder add(final String key, final boolean value) {
      properties.put(key, Boolean.valueOf(value));
      return this;
    }

    public Builder add(final String key, final double value) {
      properties.put(key, value);
      return this;
    }

    public SetPropertyStatement build() {
      return new SetPropertyStatement(opProperties, properties);
    }
  }
}
