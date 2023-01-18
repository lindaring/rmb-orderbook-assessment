package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.model.Order;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.util.Map;
import java.util.Set;

public class OrderBookServiceTest {

  private OrderBookService orderBookService;

  @Before
  public void init() {
    OrderRepository orderRepository = new OrderRepository();
    orderBookService = new OrderBookService(orderRepository);
    orderBookService.init();
    OrderBook orderBook = orderBookService.getOrderBook();
    Assert.assertNotNull(orderBook);
    Assert.assertNotNull(orderBook.buyOrdersMap());
    Assert.assertNotNull(orderBook.sellOrdersMap());
  }

  @Test
  public void BUY_ORDER_MAP_IN_DESCENDING_ORDER_TEST() {
    Map<Integer, Set<Order>> buyOrdersMap = orderBookService.getOrderBook().buyOrdersMap();
    Assert.assertNotNull(buyOrdersMap);

    Integer previousKey = null;
    for (int key: buyOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key < previousKey);
      }
      previousKey = key;
    }
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_TEST() {
    Map<Integer, Set<Order>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    Assert.assertNotNull(sellOrdersMap);

    Integer previousKey = null;
    for (int key: sellOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key > previousKey);
      }
      previousKey = key;
    }
  }
}
