package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public abstract class BaseTest {

  protected OrderRepository orderRepository;
  protected OrderBookService orderBookService;

  @Before
  public  void init() {
    orderRepository = new OrderRepository();
    orderBookService = new OrderBookService(orderRepository);
    orderBookService.init();
    OrderBook orderBook = orderBookService.getOrderBook();
    Assert.assertNotNull(orderBook);
    Assert.assertNotNull(orderBook.buyOrdersMap());
  }

  protected void verifyAscendingOrder(Map<Integer, Set<OrderEntity>> ordersMap) {
    Integer previousKey = null;
    for (int key: ordersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key > previousKey);
      }
      previousKey = key;

      LocalDateTime previousOrderTime = null;
      for (OrderEntity values: ordersMap.get(key)) {
        if (previousOrderTime != null) {
          Assert.assertTrue(previousOrderTime.isBefore(values.getTime()));
        }
        previousOrderTime = values.getTime();
      }
    }
  }

  protected void verifyDescendingOrder(Map<Integer, Set<OrderEntity>> ordersMap) {
    Integer previousKey = null;
    for (int key: ordersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key < previousKey);
      }
      previousKey = key;

      LocalDateTime previousOrderTime = null;
      for (OrderEntity values: ordersMap.get(key)) {
        if (previousOrderTime != null) {
          Assert.assertTrue(previousOrderTime.isBefore(values.getTime()));
        }
        previousOrderTime = values.getTime();
      }
    }
  }

  protected void printLimitOrderBookSide(Map<Integer, Set<OrderEntity>> ordersMap, Side side) {
    for (Integer key: ordersMap.keySet()) {
      String header = (side == Side.BUY) ? "Bid" : "Ask";
      System.out.printf("%s Level Price: %d\t", header, key);
      Set<OrderEntity> orderEntities = ordersMap.get(key);
      int count = 0;
      for (OrderEntity order: orderEntities) {
        System.out.printf("|\tBid Order Qty %d: %d\t", count, order.getQuantity());
        count++;
      }
      System.out.println();
    }
  }

}
