package com.bn.ninjatrader.model.data;


import com.bn.ninjatrader.common.data.Ichimoku;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by Brad on 4/30/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IchimokuData extends AbstractStockData<Ichimoku> {

  public IchimokuData() {
    super();
  }

  public IchimokuData(String symbol, int year) {
    super(symbol, year);
  }

  public void overlapWith(IchimokuData overlapData) {
    Map<LocalDate, Ichimoku> map = Maps.newLinkedHashMap();
    for (Ichimoku ichimoku : overlapData.getData()) {
      if (ichimoku.getDate() != null) {
        map.put(ichimoku.getDate(), ichimoku);
      }
    }

    for (Ichimoku ichimoku : getData()) {
      if (map.containsKey(ichimoku.getDate())) {
        Ichimoku overlap = map.get(ichimoku.getDate());
        ichimoku.overlapWith(overlap);
      }
    }
  }
}
