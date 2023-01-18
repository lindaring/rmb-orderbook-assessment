package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;
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
    Set<OrderEntity> ordersFromDatabase = orderRepository.getOrdersFromDatabase();

    /*
      1. Using a Map inorder to group orders per price level (as illustrated in the example limit order book table).
      2. Using TreeMap to maintain ascending/descending order based on price level. Sell side maintains ascending order
         and buy side maintains descending order based on price level.
      3. Basic operation of Treemap (containsKey, get, put and remove) at log(n) time cost.
    */
    Map<Integer, Set<OrderEntity>> sellOrderMap = new TreeMap<>();
    Map<Integer, Set<OrderEntity>> buyOrderMap = new TreeMap<>(Collections.reverseOrder());

    // Using streams for looping and to filter BUY/SELL because java can perform both operations in parallel, therefore improving performance
    if (ordersFromDatabase != null) {
      ordersFromDatabase.stream().filter(order -> order != null && order.side() == Side.BUY)
          .forEach(order -> {
            Set<OrderEntity> buyOrderSet = buyOrderMap.get(order.price());
            if (buyOrderSet == null) {
              /*
                Using TreeSet because we need to maintain ascending order in terms of time (time order was placed).
                Not using Queue(LinkedList) because it does not maintain order based on a condition. Not using
                PriorityQueue as well because it only guarantees min or max value at the top of the heap/tree but does
                not necessarily maintain ascending order for all nodes in the heap/tree based on a condition.
                TreeSet provides guaranteed log(n) time cost for the basic operations (add, remove and contains).
              */
              buyOrderSet = new TreeSet<>();
            }
            buyOrderSet.add(order);
            buyOrderMap.put(order.price(), buyOrderSet);
          });

      ordersFromDatabase.stream().filter(order -> order != null && order.side() == Side.SELL)
          .forEach(order -> {
            Set<OrderEntity> sellOrderSet = sellOrderMap.get(order.price());
            if (sellOrderSet == null) {
              // See line 38-43
              sellOrderSet = new TreeSet<>();
            }
            sellOrderSet.add(order);
            sellOrderMap.put(order.price(), sellOrderSet);
          });
    }

    this.orderBook = new OrderBook(buyOrderMap, sellOrderMap);
  }

  // TODO: Work in progress...for part 2
  public void addOrder(Side side, OrderEntity order) {
    if (this.orderBook != null) {
      if (side == Side.BUY && this.orderBook.buyOrdersMap() != null) {
        Set<OrderEntity> buyOrderSetEntity = this.orderBook.buyOrdersMap().get(order.price());
        if (buyOrderSetEntity == null) {
          // See line 38-43
          buyOrderSetEntity = new TreeSet<>();
        }
        buyOrderSetEntity.add(order);
        this.orderBook.buyOrdersMap().put(order.price(), buyOrderSetEntity);
      }
      else if (side == Side.SELL && this.orderBook.sellOrdersMap() != null) {
        Set<OrderEntity> sellOrderSet = this.orderBook.sellOrdersMap().get(order.price());
        if (sellOrderSet == null) {
          // See line 38-43
          sellOrderSet = new TreeSet<>();
        }
        sellOrderSet.add(order);
        this.orderBook.sellOrdersMap().put(order.price(), sellOrderSet);
      }
    }
  }

  // TODO: Work in progress...for part 2
  public void modifyOrder(long id, Side side, int quantity) {

  }

  // TODO: Work in progress...for part 2
  public void cancelOrder(long id, Side side) {

  }

  public OrderBook getOrderBook() {
    return orderBook;
  }

}
