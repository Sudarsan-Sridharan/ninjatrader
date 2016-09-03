package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.RSIValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RSIDocument extends ValueDocument<RSIValue> {

}
