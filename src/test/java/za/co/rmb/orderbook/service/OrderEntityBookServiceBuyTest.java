package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Test;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class OrderEntityBookServiceBuyTest extends BaseTest {

  @Test
  public void BUY_ORDER_MAP_IN_DESCENDING_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    Assert.assertNotNull(buyOrdersMap);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

  @Test
  public void BUY_ORDER_MAP_IN_DESCENDING_ORDER_AFTER_FIRST_ENTRY_REMOVED_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();

    Integer firstKey = buyOrdersMap.keySet().stream().findFirst().orElse(null);
    Assert.assertNotNull(firstKey);

    buyOrdersMap.remove(firstKey);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

  @Test
  public void BUY_ORDER_MAP_CANCEL_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    int initialMapSize = buyOrdersMap.size();

    orderBookService.cancelOrder(103L, Side.BUY);
    orderBookService.cancelOrder(105L, Side.BUY);
    int newMapSize = buyOrdersMap.size();

    boolean sizeDecreased = initialMapSize - newMapSize == 1;
    Assert.assertTrue(sizeDecreased);
    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

  @Test
  public void BUY_ORDER_MAP_MODIFY_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    orderBookService.modifyOrder(103L, Side.BUY, 10);
    //verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

  @Test
  public void BUY_ORDER_MAP_INCREASED_AND_IN_DESCENDING_ORDER_AFTER_NEW_ENTRY_ADDED_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    int initialMapSize = buyOrdersMap.size();

    OrderEntity newBuyOrder = new OrderEntity(orderRepository.nextSequence(), 11, 1, Side.BUY,
        LocalDateTime.of(2023, 1, 20, 1, 39, 45));
    orderBookService.addOrder(newBuyOrder);
    int newMapSize = buyOrdersMap.size();

    boolean sizeIncreased = newMapSize - initialMapSize == 1;
    Assert.assertTrue(sizeIncreased);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookSide(buyOrdersMap, Side.BUY);
  }

}
