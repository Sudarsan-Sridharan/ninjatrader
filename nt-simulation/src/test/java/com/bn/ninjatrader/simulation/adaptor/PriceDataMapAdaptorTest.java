package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.DataMap;
import org.junit.Before;
import org.junit.Test;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/18/16.
 */
public class PriceDataMapAdaptorTest {

  private PriceDataMapAdaptor adaptor;

  @Before
  public void setup() {
    adaptor = new PriceDataMapAdaptor();
  }

  @Test
  public void testToDataMap() {
    final Price price = TestUtil.randomPriceBuilder().volume(1000000000000l).build();
    final DataMap dataMap = adaptor.toDataMap(price);

    assertThat(dataMap.get(PRICE_OPEN)).isEqualTo(price.getOpen());
    assertThat(dataMap.get(PRICE_HIGH)).isEqualTo(price.getHigh());
    assertThat(dataMap.get(PRICE_LOW)).isEqualTo(price.getLow());
    assertThat(dataMap.get(PRICE_CLOSE)).isEqualTo(price.getClose());
    assertThat(dataMap.get(VOLUME).longValue()).isEqualTo(price.getVolume());
  }
}
