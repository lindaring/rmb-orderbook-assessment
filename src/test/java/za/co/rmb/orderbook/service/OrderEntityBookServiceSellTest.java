package za.co.rmb.orderbook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class OrderEntityBookServiceSellTest {

  private OrderRepository orderRepository;
  private OrderBookService orderBookService;

  @Before
  public void init() {
    orderRepository = new OrderRepository();
    orderBookService = new OrderBookService(orderRepository);
    orderBookService.init();
    OrderBook orderBook = orderBookService.getOrderBook();
    Assert.assertNotNull(orderBook);
    Assert.assertNotNull(orderBook.sellOrdersMap());
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();
    Assert.assertNotNull(sellOrdersMap);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookAskSide(sellOrdersMap);
  }

  @Test
  public void SELL_ORDER_MAP_IN_ASCENDING_ORDER_AFTER_FIRST_ENTRY_REMOVED_TEST() {
    Map<Integer, Set<OrderEntity>> sellOrdersMap = orderBookService.getOrderBook().sellOrdersMap();

    Integer firstKey = sellOrdersMap.keySet().stream().findFirst().orElse(null);
    Assert.assertNotNull(firstKey);

    sellOrdersMap.remove(firstKey);

    verifyAscendingOrder(sellOrdersMap);
    printLimitOrderBookAskSide(sellOrdersMap);
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
    printLimitOrderBookAskSide(sellOrdersMap);
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
    printLimitOrderBookAskSide(sellOrdersMap);
  }

  private void verifyAscendingOrder(Map<Integer, Set<OrderEntity>> sellOrdersMap) {
    Integer previousKey = null;
    for (int key: sellOrdersMap.keySet()) {
      if (previousKey != null) {
        Assert.assertTrue(key > previousKey);
      }
      previousKey = key;

      LocalDateTime previousOrderTime = null;
      for (OrderEntity values: sellOrdersMap.get(key)) {
        if (previousOrderTime != null) {
          Assert.assertTrue(previousOrderTime.isBefore(values.time()));
        }
        previousOrderTime = values.time();
      }
    }
  }

  private void printLimitOrderBookAskSide(Map<Integer, Set<OrderEntity>> sellOrdersMap) {
    for (Integer key: sellOrdersMap.keySet()) {
      System.out.printf("Ask Level Price: %d\t", key);
      Set<OrderEntity> orderEntities = sellOrdersMap.get(key);
      int count = 0;
      for (OrderEntity order: orderEntities) {
        System.out.printf("|\tAsk Order Qty %d: %d\t", count, order.quantity());
        count++;
      }
      System.out.println();
    }
  }

}
