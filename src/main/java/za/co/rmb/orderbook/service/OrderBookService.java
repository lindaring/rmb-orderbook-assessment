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

    Map<Integer, Map<Long, OrderEntity>> buySide = initSide(ordersFromDatabase, Side.BUY);
    Map<Integer, Map<Long, OrderEntity>> sellSide = initSide(ordersFromDatabase, Side.SELL);

    this.orderBook = new OrderBook(buySide, sellSide);
  }

  private Map<Integer, Map<Long, OrderEntity>> initSide(Set<OrderEntity> allOrders, Side sideType) {
    /*
      1. Using a Map inorder to group orders per price level (as illustrated in the example limit order book table).
      2. Using TreeMap to maintain ascending/descending order based on price level. Sell side maintains ascending order
         and buy side maintains descending order based on price level.
      3. Basic operation of TreeMap (containsKey, get, put and remove) at log(n) time cost.
    */
    Map<Integer, Map<Long, OrderEntity>> oderBookSide = (sideType == Side.BUY) ?
        new TreeMap<>(Collections.reverseOrder()) :
        new TreeMap<>();

    if (allOrders == null) {
      return oderBookSide;
    }

    // Using streams for looping and to filter BUY/SELL because java can perform both operations in parallel, therefore improving performance
    allOrders.stream()
        .filter(order -> order != null && order.side() == sideType)
        .forEach(order -> {
          Map<Long, OrderEntity> sideMap = oderBookSide.get(order.price());
          if (sideMap == null) {
              /*
                Using TreeMap because we need to maintain ascending order in terms of time (time order was placed) and also
                storing key=orderId in order to retrieve order by orderId efficiently.
                ----------------------------------------------------------------------
                Not using Queue(LinkedList) because it does not maintain order based on a condition. Not using
                PriorityQueue as well because it only guarantees min or max value at the top of the heap/tree but does
                not necessarily maintain ascending order for all nodes in the heap/tree based on a condition.
                TreeSet provides guaranteed log(n) time cost for the basic operations (add, remove and contains).
              */
            sideMap = new TreeMap<>();
          }
          sideMap.put(order.id(), order);
          oderBookSide.put(order.price(), sideMap);
        });

    return oderBookSide;
  }

  public void addOrder(Side side, OrderEntity order) {
    if (this.orderBook == null) {
      return;
    }

    Map<Integer, Map<Long, OrderEntity>> orderBookSide = (side == Side.BUY) ?
        this.orderBook.buyOrdersMap() :
        this.orderBook.sellOrdersMap();

    Map<Long, OrderEntity> sideMap = orderBookSide.get(order.price());
    if (sideMap == null) {
      // See line 51-57
      sideMap = new TreeMap<>();
    }

    sideMap.put(order.id(), order);
    orderBookSide.put(order.price(), sideMap);
  }

  // TODO: Work in progress...for part 2
  public void modifyOrder(long id, Side side, int quantity) {
  }

  public void cancelOrder(long id, Side side) {
    if (this.orderBook == null) {
      return;
    }

    Map<Integer, Map<Long, OrderEntity>> orderBookSide = (side == Side.BUY) ?
        this.orderBook.buyOrdersMap() :
        this.orderBook.sellOrdersMap();

    for (Integer priceKey: orderBookSide.keySet()) {
      Map<Long, OrderEntity> priceValueMap = orderBookSide.get(priceKey);
      if (priceValueMap.containsKey(id)) {
        priceValueMap.remove(id);
        if (priceValueMap.isEmpty()) {
          orderBookSide.remove(priceKey);
        }
        return;
      }
    }
  }

  public OrderBook getOrderBook() {
    return orderBook;
  }

}
