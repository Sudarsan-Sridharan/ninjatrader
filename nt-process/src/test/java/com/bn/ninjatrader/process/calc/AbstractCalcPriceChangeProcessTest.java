package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import mockit.Expectations;

import java.util.List;

/**
 * Created by Brad on 8/30/16.
 */
public class AbstractCalcPriceChangeProcessTest {

  final Price price1 = TestUtil.randomPrice();
  final Price price2 = TestUtil.randomPrice();
  final Price price3 = TestUtil.randomPrice();

  final Price processedPrice1 = TestUtil.randomPrice();
  final Price processedPrice2 = TestUtil.randomPrice();
  final Price processedPrice3 = TestUtil.randomPrice();

  final List<Price> priceList = Lists.newArrayList(price1, price2, price3);

  final List<Price> processedPriceList = Lists.newArrayList(processedPrice1, processedPrice2, processedPrice3);

  public void setup(PriceDao priceDao, PriceChangeCalculator calculator) {
    new Expectations() {{
      priceDao.find(withInstanceOf(FindRequest.class));
      result = priceList;
      times = 1;

      calculator.calc(priceList);
      result = processedPriceList;
      times = 1;
    }};
  }
}
