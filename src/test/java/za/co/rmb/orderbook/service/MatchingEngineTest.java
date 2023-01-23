package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Test;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.enumerator.Side;

import java.util.Map;
import java.util.Set;

public class MatchingEngineTest extends BaseTest {

  @Test
  public void PLACE_BUY_QUANTITY_55_CHECK_REMAINING_QUANTITY_IS_5_ORDER() throws Exception {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();

    int quantity = orderBookService.placeBuyOrder(9, 55);
    Assert.assertEquals(0, quantity);

    OrderEntity orderLeft = buyOrdersMap.get(9).stream().findFirst().orElse(null);
    Assert.assertNotNull(orderLeft);
    Assert.assertEquals(5, (int) orderLeft.getQuantity());

    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

  @Test
  public void PLACE_BUY_QUANTITY_60_CHECK_REMAINING_QUANTITY_IS_0_ORDER() throws Exception {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();

    int quantity = orderBookService.placeBuyOrder(9, 60);
    Assert.assertEquals(0, quantity);
    Assert.assertFalse(buyOrdersMap.containsKey(9));

    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }
}
