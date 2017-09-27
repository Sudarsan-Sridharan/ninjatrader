package com.bn.ninjatrader.service.push;

import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanUpdatePushMessage {

  @JsonProperty("algorithmId")
  private final String algorithmId;

  @JsonProperty("scanResults")
  private final List<ScanResult> scanResults;

  public ScanUpdatePushMessage(@JsonProperty("algorithmId") final String algorithmId,
                               @JsonProperty("scanResults") final List<ScanResult> scanResults) {
    this.algorithmId = algorithmId;
    this.scanResults = scanResults;
  }

  public String getAlgorithmId() {
    return algorithmId;
  }

  public List<ScanResult> getScanResults() {
    return scanResults;
  }
}
