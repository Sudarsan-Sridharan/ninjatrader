package com.bn.ninjatrader.server.http;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.rest.PriceResponse;
import com.bn.ninjatrader.common.type.Period;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.DataFinder;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import mockit.*;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 5/27/16.
 */
public class PriceHttpServiceTest {

  @Tested
  private PriceHttpService priceService;

  @Injectable
  private PriceDao priceDao;

  @Injectable
  private WeeklyPriceDao weeklyPriceDao;

  @Mocked
  private PriceUtil priceUtil;

  @Test
  public void testGetStockPrice() {
    // Prepare data
    Price price1 = new Price(LocalDate.now(), 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(LocalDate.now().plusDays(1), 2.1, 2.2, 2.0, 2.1, 20000);
    Price priceSummary = new Price(LocalDate.now(), 3.1, 3.2, 3.0, 3.1, 30000);
    List<Price> priceList = Lists.newArrayList(price1, price2);

    new Expectations() {{
      priceDao.find(withInstanceOf(FindRequest.class));
      result = priceList;

      PriceUtil.createSummary(withInstanceOf(List.class)); result = priceSummary;
    }};

    Optional<PriceResponse> foundPriceData = priceService.getPriceResponse("MEG");
    assertTrue(foundPriceData.isPresent());

    // Verify PriceData
    PriceResponse priceData = foundPriceData.get();
    assertEquals(priceData.getFromDate(), price1.getDate());
    assertEquals(priceData.getToDate(), price2.getDate());
    assertNotNull(priceData.getPriceSummary());
    TestUtil.assertPriceEquals(priceData.getPriceSummary(), priceSummary);

    // Verify Price list
    List<Price> resultPriceList = priceData.getPriceList();
    assertNotNull(resultPriceList);
    assertEquals(resultPriceList.size(), 2);
    TestUtil.assertPriceEquals(resultPriceList.get(0), price1);
    TestUtil.assertPriceEquals(resultPriceList.get(1), price2);
  }

  @Test
  public void testGetPriceForDiffPeriods() {
    priceService.getPriceResponse("MEG", Period.DAILY);
    assertFindForSymbolCalled(priceDao, "MEG");

    priceService.getPriceResponse("BDO", Period.WEEKLY);
    assertFindForSymbolCalled(weeklyPriceDao, "BDO");
  }

  public void assertFindForSymbolCalled(DataFinder dataDao, String symbol) {
    new Verifications() {{
      FindRequest findRequest;
      dataDao.find(findRequest = withCapture());
      assertEquals(findRequest.getSymbol(), symbol);
    }};
  }

}
