package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ichimoku implements DateObj<Ichimoku> {

  public static final Builder builder() {
    return new Builder();
  }

  private static final Ichimoku EMPTY_INSTANCE = new Ichimoku();

  @JsonProperty("t")
  private double tenkan;

  @JsonProperty("k")
  private double kijun;

  @JsonProperty("c")
  private double chikou;

  @JsonProperty("sa")
  private double senkouA;

  @JsonProperty("sb")
  private double senkouB;

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  public static final Ichimoku empty() {
    return EMPTY_INSTANCE;
  }

  public Ichimoku() {}

  public Ichimoku(LocalDate date, double chikou, double tenkan, double kijun, double senkouA, double senkouB) {
    this.date = date;
    this.chikou = chikou;
    this.tenkan = tenkan;
    this.kijun = kijun;
    this.senkouA = senkouA;
    this.senkouB = senkouB;
  }

  public double getTenkan() {
    return tenkan;
  }

  public void setTenkan(double tenkan) {
    this.tenkan = tenkan;
  }

  public double getKijun() {
    return kijun;
  }

  public void setKijun(double kijun) {
    this.kijun = kijun;
  }

  public double getChikou() {
    return chikou;
  }

  public void setChikou(double chikou) {
    this.chikou = chikou;
  }

  public double getSenkouA() {
    return senkouA;
  }

  public void setSenkouA(double senkouA) {
    this.senkouA = senkouA;
  }

  public double getSenkouB() {
    return senkouB;
  }

  public void setSenkouB(double senkouB) {
    this.senkouB = senkouB;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) { return false; }
    if (obj == this) { return true; }
    final Ichimoku rhs = (Ichimoku) obj;
    return Objects.equal(date, rhs.date)
        && Objects.equal(tenkan, rhs.tenkan)
        && Objects.equal(kijun, rhs.kijun)
        && Objects.equal(chikou, rhs.chikou)
        && Objects.equal(senkouA, rhs.senkouA)
        && Objects.equal(senkouB, rhs.senkouB);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, tenkan, kijun, chikou, senkouA, senkouB);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("d", date).add("t", tenkan).add("k", kijun).add("c", chikou).add("sa", senkouA).add("sb", senkouB)
        .toString();
  }

  @Override
  public int compareTo(Ichimoku o) {
    return 0;
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private LocalDate date;
    private double tenkan;
    private double kijun;
    private double chikou;
    private double senkouA;
    private double senkouB;

    public Builder copyOf(final Ichimoku ichimoku) {
      this.date = ichimoku.date;
      this.tenkan = ichimoku.tenkan;
      this.kijun = ichimoku.kijun;
      this.chikou = ichimoku.chikou;
      this.senkouA = ichimoku.senkouA;
      this.senkouB = ichimoku.senkouB;
      return this;
    }
    public Builder date(final LocalDate date) {
      this.date = date;
      return this;
    }
    public Builder tenkan(final double tenkan) {
      this.tenkan = tenkan;
      return this;
    }
    public Builder kijun(final double kijun) {
      this.kijun = kijun;
      return this;
    }
    public Builder chikou(final double chikou) {
      this.chikou = chikou;
      return this;
    }
    public Builder senkouA(final double senkouA) {
      this.senkouA = senkouA;
      return this;
    }
    public Builder senkouB(final double senkouB) {
      this.senkouB = senkouB;
      return this;
    }

    public double getTenkan() {
      return tenkan;
    }

    public Ichimoku build() {
      return new Ichimoku(date, chikou, tenkan, kijun, senkouA, senkouB);
    }
  }
}
