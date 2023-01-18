package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.model.Order;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.util.*;

public class OrderBookService {
  private final OrderRepository orderRepository;

  private OrderBook orderBook = null;

  public OrderBookService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public void init() {
    // Note comment in the getOrdersFromDatabase method
    Set<Order> ordersFromDatabase = orderRepository.getOrdersFromDatabase();

    /*
      1. Using a Map inorder to group orders per price level (as illustrated in the example limit order book table).
      2. Using TreeMap to maintain ascending/descending order based on price level. Sell side maintains ascending order
         and buy side maintains descending order based on price level.
      3. Basic operation (containsKey, get, put and remove) at log(n) time cost.
    */
    Map<Integer, Set<Order>> sellOrderMap = new TreeMap<>();
    Map<Integer, Set<Order>> buyOrderMap = new TreeMap<>(Collections.reverseOrder());

    // Using streams for filter BUY / SELL because java can perform both operations in parallel, therefore improving performance
    if (ordersFromDatabase != null) {
      ordersFromDatabase.stream().filter(order -> order != null && order.side() == Side.BUY)
          .forEach(order -> {
            Set<Order> buyOrderQueue = buyOrderMap.get(order.price());
            if (buyOrderQueue == null) {
              /*
                Using TreeSet because we need to maintain ascending order in terms of time. Not using PriorityQueue because
                It only guarantees min/max value at the top but does not necessarily maintain ascending order.
                TreeSet provides guaranteed log(n) time cost for the basic operations (add, remove and contains).
              */
              buyOrderQueue = new TreeSet<>();
            }
            buyOrderQueue.add(order);
            buyOrderMap.put(order.price(), buyOrderQueue);
          });

      ordersFromDatabase.stream().filter(order -> order != null && order.side() == Side.SELL)
          .forEach(order -> {
            Set<Order> sellOrderQueue = sellOrderMap.get(order.price());
            if (sellOrderQueue == null) {
              // Above comment applies...
              sellOrderQueue = new TreeSet<>();
            }
            sellOrderQueue.add(order);
            sellOrderMap.put(order.price(), sellOrderQueue);
          });
    }

    this.orderBook = new OrderBook(buyOrderMap, sellOrderMap);
  }

  public OrderBook getOrderBook() {
    return orderBook;
  }

}
