package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.DataMap;

/**
 * @author bradwee2000@gmail.com
 */
public interface BindingProvider {

  DataMap get(final Price price);
}
