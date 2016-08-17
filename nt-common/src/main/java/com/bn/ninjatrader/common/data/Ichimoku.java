package com.bn.ninjatrader.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ichimoku implements DateObj<Ichimoku> {

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
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
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
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("d", date)
        .append("t", tenkan)
        .append("k", kijun)
        .append("c", chikou)
        .append("sa", senkouA)
        .append("sb", senkouB)
        .toString();
  }

  public void overlapWith(Ichimoku overlap) {
    if (overlap.getChikou() > 0d) {
      chikou = overlap.getChikou();
    }
    if (overlap.getTenkan() > 0d) {
      tenkan = overlap.getTenkan();
    }
    if (overlap.getKijun() > 0d) {
      kijun = overlap.getKijun();
    }
    if (overlap.getSenkouA() > 0d) {
      senkouA = overlap.getSenkouA();
    }
    if (overlap.getSenkouB() > 0d) {
      senkouB = overlap.getSenkouB();
    }
  }

  @Override
  public int compareTo(Ichimoku o) {
    return 0;
  }
}
