package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalTime;
import java.util.Map;

public class OrderEntityBookServiceSellTest {

  private OrderBookService orderBookService;

  @Before
  public void init() {
    OrderRepository orderRepository = new OrderRepository();
    orderBookService = new OrderBookService(orderRepository);
    orderBookService.init();
    OrderBook orderBook = orderBookService.getOrderBook();
    Assert.assertNotNull(orderBook);
    Assert.assertNotNull(orderBook.sellOrdersMap());
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_TEST() {
    Map<Integer, Map<Long, OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    Assert.assertNotNull(sellOrdersMap);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookAskSide(sellOrdersMap);
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_AFTER_FIRST_ENTRY_REMOVED_TEST() {
    Map<Integer, Map<Long, OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();

    Integer firstKey = sellOrdersMap.keySet().stream().findFirst().orElse(null);
    Assert.assertNotNull(firstKey);

    sellOrdersMap.remove(firstKey);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookAskSide(sellOrdersMap);
  }

  @Test
  public void SELL_ORDER_MAP_INCREASED_AND_IN_ASCENDING_ORDER_AFTER_NEW_ENTRY_ADDED_TEST() {
    Map<Integer, Map<Long, OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    int initialMapSize = sellOrdersMap.size();

    OrderEntity newSellOrder = new OrderEntity(OrderRepository.nextSequence(), 31, 44, Side.SELL,
        LocalTime.of(20, 2, 24));
    orderBookService.addOrder(Side.SELL, newSellOrder);
    int newMapSize = sellOrdersMap.size();

    boolean sizeIncreased = newMapSize - initialMapSize == 1;
    Assert.assertTrue(sizeIncreased);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookAskSide(sellOrdersMap);
  }

  private void verifyAscendingOrder(Map<Integer, Map<Long, OrderEntity>> sellOrdersMap) {
    Integer previousKey = null;
    for (int key: sellOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key > previousKey);
      }
      previousKey = key;
    }
  }

  private void printLimitOrderBookAskSide(Map<Integer, Map<Long, OrderEntity>> sellOrdersMap) {
    for (Integer key: sellOrdersMap.keySet()) {
      System.out.printf("Ask Level Price: %d\t", key);
      Map<Long, OrderEntity> orderEntities = sellOrdersMap.get(key);
      int count = 0;
      for (OrderEntity order: orderEntities.values()) {
        System.out.printf("|\tAsk Order Qty %d: %d\t", count, order.quantity());
        count++;
      }
      System.out.println();
    }
  }

}
