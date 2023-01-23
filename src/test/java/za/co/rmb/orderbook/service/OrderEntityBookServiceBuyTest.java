package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class OrderEntityBookServiceBuyTest {

  private OrderRepository orderRepository;
  private OrderBookService orderBookService;

  @Before
  public void init() {
    orderRepository = new OrderRepository();
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
  public void BUY_ORDER_MAP_CANCEL_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    int initialMapSize = buyOrdersMap.size();

    orderBookService.cancelOrder(103L, Side.BUY);
    orderBookService.cancelOrder(105L, Side.BUY);
    int newMapSize = buyOrdersMap.size();

    boolean sizeDecreased = initialMapSize - newMapSize == 1;
    Assert.assertTrue(sizeDecreased);
    printLimitOrderBookBidSide(buyOrdersMap);
  }

  @Test
  public void BUY_ORDER_MAP_MODIFY_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    orderBookService.modifyOrder(103L, Side.BUY, 10);
    verifyDescendingOrder(buyOrdersMap);
    printLimitOrderBookBidSide(buyOrdersMap);
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
    printLimitOrderBookBidSide(buyOrdersMap);
  }

  private void verifyDescendingOrder(Map<Integer, Set<OrderEntity>> buyOrdersMap) {
    Integer previousKey = null;
    for (int key: buyOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key < previousKey);
      }
      previousKey = key;

      LocalDateTime previousOrderTime = null;
      for (OrderEntity values: buyOrdersMap.get(key)) {
        if (previousOrderTime != null) {
          Assert.assertTrue(previousOrderTime.isBefore(values.time()));
        }
        previousOrderTime = values.time();
      }
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
