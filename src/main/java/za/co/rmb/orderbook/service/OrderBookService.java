package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;
import za.co.rmb.orderbook.exception.NotFoundException;
import za.co.rmb.orderbook.exception.OrderBookException;
import za.co.rmb.orderbook.model.OrderBook;
import za.co.rmb.orderbook.repository.OrderRepository;

import java.time.LocalDateTime;
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

    Map<Integer, Set<OrderEntity>> buySide = initSide(ordersFromDatabase, Side.BUY);
    Map<Integer, Set<OrderEntity>> sellSide = initSide(ordersFromDatabase, Side.SELL);

    this.orderBook = new OrderBook(buySide, sellSide);
  }

  private Map<Integer, Set<OrderEntity>> initSide(Set<OrderEntity> allOrders, Side sideType) {
    /*
      1. Using a Map in-order to group orders per price level (as illustrated in the example limit order book table).
      2. Using TreeMap to maintain ascending/descending order based on price level. Sell side maintains ascending order
         and buy side maintains descending order based on price level.
      3. Basic operation of TreeMap (containsKey, get, put and remove) at log(n) time cost.
    */
    Map<Integer, Set<OrderEntity>> oderBookSide = (sideType == Side.BUY) ?
        new TreeMap<>(Collections.reverseOrder()) :
        new TreeMap<>();

    if (allOrders == null) {
      return oderBookSide;
    }

    // Using streams for looping and to filter BUY/SELL because java can perform both operations in parallel, therefore improving performance
    allOrders.stream()
        .filter(order -> order != null && order.getSide() == sideType)
        .forEach(order -> {
          Set<OrderEntity> sideMap = oderBookSide.get(order.getPrice());
          if (sideMap == null) {
              /*
                Using TreeSet because we need to maintain ascending order in terms of time (time order was placed).
                Not using Queue(LinkedList) because it does not maintain order based on a condition. Not using
                PriorityQueue as well because it only guarantees min or max value at the top of the heap/tree but does
                not necessarily maintain ascending order for all nodes in the heap/tree based on a condition.
                TreeSet provides guaranteed log(n) time cost for the basic operations (add, remove and contains).
              */
            sideMap = new TreeSet<>();
          }
          sideMap.add(order);
          oderBookSide.put(order.getPrice(), sideMap);
        });

    return oderBookSide;
  }

  public void addOrder(OrderEntity order) {
    if (this.orderBook == null) {
      return;
    }

    Map<Integer, Set<OrderEntity>> orderBookSide = (order.getSide() == Side.BUY) ?
        this.orderBook.buyOrdersMap() :
        this.orderBook.sellOrdersMap();

    Set<OrderEntity> sideSet = orderBookSide.get(order.getPrice());
    if (sideSet == null) {
      // See line 53-59 Comment
      sideSet = new TreeSet<>();
    }

    sideSet.add(order);
    orderBookSide.put(order.getPrice(), sideSet);
  }

  public void modifyOrder(long id, Side side, int quantity) {
    OrderEntity previousOrder = cancelOrder(id, side);
    if (previousOrder != null) {
      addOrder(new OrderEntity(orderRepository.nextSequence(), previousOrder.getPrice(), quantity, side, LocalDateTime.now()));
    }
  }

  public OrderEntity cancelOrder(long id, Side side) {
    if (this.orderBook == null) {
      return null;
    }

    Map<Integer, Set<OrderEntity>> orderBookSide = (side == Side.BUY) ?
        this.orderBook.buyOrdersMap() :
        this.orderBook.sellOrdersMap();

    for (Integer priceKey: orderBookSide.keySet()) {
      Set<OrderEntity> priceSet = orderBookSide.get(priceKey);
      for (OrderEntity order: priceSet) {
        if (order.getId() == id) {
          priceSet.remove(order);
          if (priceSet.isEmpty()) {
            orderBookSide.remove(priceKey);
          }
          return order;
        }
      }
    }
    return null;
  }

  /**
   * Places and order based on the available quantity.
   * @return =0 is the quantity was fulfilled, or >0 if the order was partially fulfilled.
   */
  public int placeBuyOrder(int price, int requestedQuantity) throws OrderBookException, NotFoundException {
    if (this.orderBook == null || orderBook.buyOrdersMap() == null) {
      throw new OrderBookException("Limit order book not set");
    }

    // Get all quantities for a given price point
    Set<OrderEntity> orderSet = orderBook.buyOrdersMap().get(price);
    if (orderSet == null || orderSet.isEmpty()) {
      throw new NotFoundException("Price not found");
    }

    // Place order. Remove item if quantity left is 0. Return the request quantity that was not fulfilled.
    Iterator<OrderEntity> iterator = orderSet.iterator();
    while (iterator.hasNext()) {
      OrderEntity order = iterator.next();
      if (requestedQuantity == 0) {
        break;
      }
      else if (order.getQuantity() - requestedQuantity == 0) {
        requestedQuantity = 0;
        iterator.remove();
      }
      else if (order.getQuantity() - requestedQuantity > 0) {
        order.setQuantity(order.getQuantity() - requestedQuantity);
        requestedQuantity = 0;
      }
      else {// if quantityNotFulfilled < 0
        requestedQuantity -= order.getQuantity();
        iterator.remove();
      }
    }

    if (orderSet.isEmpty()) {
      orderBook.buyOrdersMap().remove(price); // Not is left
    }
    return requestedQuantity;
  }

  public OrderBook getOrderBook() {
    return orderBook;
  }

}
