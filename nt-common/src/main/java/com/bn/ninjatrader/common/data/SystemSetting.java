package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateTimeDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by Brad on 7/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemSetting {

  @JsonProperty("name")
  private String name;

  @JsonProperty("value")
  private String value;

  @JsonProperty("lud")
  @JsonSerialize(using = NtLocalDateTimeSerializer.class)
  @JsonDeserialize(using = NtLocalDateTimeDeserializer.class)
  private LocalDateTime lastUpdateDate;

  public static SystemSetting of(String name, String value) {
    return new SystemSetting(name, value);
  }

  public SystemSetting() {}

  public SystemSetting(String name, String value) {
    this.name = name;
    this.value = value;
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
    if (!(obj instanceof SystemSetting)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    SystemSetting rhs = (SystemSetting) obj;
    return new EqualsBuilder()
        .append(name, rhs.getName())
        .append(value, rhs.getValue())
        .append(lastUpdateDate, rhs.getLastUpdateDate())
        .isEquals();
  }
}

