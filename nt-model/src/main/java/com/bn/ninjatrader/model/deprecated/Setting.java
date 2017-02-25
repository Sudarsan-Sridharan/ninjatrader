package com.bn.ninjatrader.model.deprecated;

import com.bn.ninjatrader.common.util.NumUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by Brad on 7/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setting {

  @JsonProperty("n")
  private String name;

  @JsonProperty("v")
  private String value;

  //TODO change to proper format
  @JsonProperty("lud")
  private LocalDateTime lastUpdateDate;

  public static Setting of(String name, String value) {
    return new Setting(name, value);
  }

  public Setting(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public Setting(@JsonProperty("n") String name,
                 @JsonProperty("v") String value,
                 @JsonProperty("lud") LocalDateTime lastUpdateDate) {
    this.name = name;
    this.value = value;
    this.lastUpdateDate = lastUpdateDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public long getValueAsLong() {
    return NumUtil.toLongOrDefault(value, 0);
  }

  public double getValueAsDouble() {
    return NumUtil.toDoubleOrDefault(value, 0);
  }

  public void setValue(String value) {
    this.value = value;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("Name", name)
        .append("Value", value)
        .append("Last Update", lastUpdateDate)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(name)
        .append(value)
        .append(lastUpdateDate)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Setting)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Setting rhs = (Setting) obj;
    return new EqualsBuilder()
        .append(name, rhs.getName())
        .append(value, rhs.getValue())
        .append(lastUpdateDate, rhs.getLastUpdateDate())
        .isEquals();
  }
}

