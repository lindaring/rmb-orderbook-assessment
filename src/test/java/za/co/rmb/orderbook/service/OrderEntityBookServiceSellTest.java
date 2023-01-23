package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Test;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.enumerator.Side;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class OrderEntityBookServiceSellTest extends BaseTest {

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    Assert.assertNotNull(sellOrdersMap);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookSide(sellOrdersMap, Side.SELL);
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_AFTER_FIRST_ENTRY_REMOVED_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();

    Integer firstKey = sellOrdersMap.keySet().stream().findFirst().orElse(null);
    Assert.assertNotNull(firstKey);

    sellOrdersMap.remove(firstKey);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookSide(sellOrdersMap, Side.SELL);
  }

  @Test
  public void SELL_ORDER_MAP_CANCEL_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    int initialMapSize = sellOrdersMap.size();

    orderBookService.cancelOrder(109L, Side.SELL);
    orderBookService.cancelOrder(111L, Side.SELL);
    int newMapSize = sellOrdersMap.size();

    boolean sizeDecreased = initialMapSize - newMapSize == 1;
    Assert.assertTrue(sizeDecreased);
    printLimitOrderBookSide(sellOrdersMap, Side.SELL);
  }

  @Test
  public void SELL_ORDER_MAP_INCREASED_AND_IN_ASCENDING_ORDER_AFTER_NEW_ENTRY_ADDED_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    int initialMapSize = sellOrdersMap.size();

    OrderEntity newSellOrder = new OrderEntity(orderRepository.nextSequence(), 31, 44, Side.SELL,
        LocalDateTime.of(2023, 1, 20, 20, 2, 24));
    orderBookService.addOrder(newSellOrder);
    int newMapSize = sellOrdersMap.size();

    boolean sizeIncreased = newMapSize - initialMapSize == 1;
    Assert.assertTrue(sizeIncreased);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookSide(sellOrdersMap, Side.SELL);
  }

  @Test
  public void SELL_ORDER_MAP_MODIFY_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    orderBookService.modifyOrder(109L, Side.SELL, 10);
    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookSide(sellOrdersMap, Side.SELL);
  }


}
