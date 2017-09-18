package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.data.DataMap;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
public interface BindingProvider extends Serializable {

  DataMap get(final Price price);
}
