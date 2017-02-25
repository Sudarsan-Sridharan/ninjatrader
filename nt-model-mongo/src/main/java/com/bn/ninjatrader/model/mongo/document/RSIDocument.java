package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.model.deprecated.RSIValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RSIDocument extends ValueDocument<RSIValue> {

}
