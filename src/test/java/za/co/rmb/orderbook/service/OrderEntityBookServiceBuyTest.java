package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public class OrderEntityBookServiceBuyTest {

  private OrderBookService orderBookService;

  @Before
  public void init() {
    OrderRepository orderRepository = new OrderRepository();
    orderBookService = new OrderBookService(orderRepository);
    orderBookService.init();
    OrderBook orderBook = orderBookService.getOrderBook();
    Assert.assertNotNull(orderBook);
    Assert.assertNotNull(orderBook.buyOrdersMap());
  }

  @Test
  public void BUY_ORDER_MAP_IN_DESCENDING_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    Assert.assertNotNull(buyOrdersMap);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookBidSide(buyOrdersMap);
  }

  @Test
  public void BUY_ORDER_MAP_IN_DESCENDING_ORDER_AFTER_FIRST_ENTRY_REMOVED_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();

    Integer firstKey = buyOrdersMap.keySet().stream().findFirst().orElse(null);
    Assert.assertNotNull(firstKey);

    buyOrdersMap.remove(firstKey);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookBidSide(buyOrdersMap);
  }

  @Test
  public void BUY_ORDER_MAP_INCREASED_AND_IN_DESCENDING_ORDER_AFTER_NEW_ENTRY_ADDED_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    int initialMapSize = buyOrdersMap.size();

    OrderEntity newBuyOrder = new OrderEntity(OrderRepository.nextSequence(), 35, 1, Side.BUY,
        LocalTime.of(1, 39, 45));
    orderBookService.addOrder(Side.BUY, newBuyOrder);
    int newMapSize = buyOrdersMap.size();

    boolean sizeIncreased = newMapSize - initialMapSize == 1;
    Assert.assertTrue(sizeIncreased);

    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookBidSide(buyOrdersMap);
  }

  private static void verifyDescendingOrder(Map<Integer, Set<OrderEntity>> buyOrdersMap) {
    Integer previousKey = null;
    for (int key: buyOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key < previousKey);
      }
      previousKey = key;
    }
  }

  private void printLimitOrderBookBidSide(Map<Integer, Set<OrderEntity>> buyOrdersMap) {
    for (Integer key: buyOrdersMap.keySet()) {
      System.out.printf("Bid Level Price: %d\t", key);
      Set<OrderEntity> orderEntities = buyOrdersMap.get(key);
      int count = 0;
      for (OrderEntity order: orderEntities) {
        System.out.printf("|\tBid Order Qty %d: %d\t", count, order.quantity());
        count++;
      }
      System.out.println();
    }
  }
}
