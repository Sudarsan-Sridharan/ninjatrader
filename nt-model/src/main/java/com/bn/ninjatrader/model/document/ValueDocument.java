package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueDocument<T extends Value> extends AbstractPerPeriodDocument<T> {

}
