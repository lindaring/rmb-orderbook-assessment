package za.co.rmb.orderbook.repository;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.model.Order;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Note: This class is to simulate a call to the database to get list of orders.
 */
public class OrderRepository {

  public Set<Order> getOrdersFromDatabase() {
    // Using a HashSet here because I do not care about order or indexes. Since the order and indexes are irrelevant at this
    // point, we can take advantage of HashSet performance basic operations (add, remove, contains and size) at constant time.
    Set<Order> ordersFromDatabase = new HashSet<>();

    // Buys orders
    ordersFromDatabase.add(new Order(101L, 33, 10, Side.BUY, LocalTime.of(12, 3,18)));
    ordersFromDatabase.add(new Order(102L, 33, 5, Side.BUY, LocalTime.of(11, 30,1)));
    ordersFromDatabase.add(new Order(103L, 34, 7, Side.BUY, LocalTime.of(12, 0,22)));
    ordersFromDatabase.add(new Order(104L, 35, 12, Side.BUY, LocalTime.of(13, 50,45)));
    ordersFromDatabase.add(new Order(105L, 34, 14, Side.BUY, LocalTime.of(13, 24,23)));
    ordersFromDatabase.add(new Order(111L, 34, 9, Side.BUY, LocalTime.of(13, 24,23)));

    // Sell orders
    ordersFromDatabase.add(new Order(113L, 30, 11, Side.SELL, LocalTime.of(15, 5,11)));
    ordersFromDatabase.add(new Order(106L, 30, 6, Side.SELL, LocalTime.of(9, 31,59)));
    ordersFromDatabase.add(new Order(107L, 35, 9, Side.SELL, LocalTime.of(10, 14,16)));
    ordersFromDatabase.add(new Order(108L, 35, 11, Side.SELL, LocalTime.of(12, 44,36)));
    ordersFromDatabase.add(new Order(109L, 29, 8, Side.SELL, LocalTime.of(14, 16,46)));
    ordersFromDatabase.add(new Order(110L, 30, 10, Side.SELL, LocalTime.of(10, 33,21)));
    ordersFromDatabase.add(new Order(112L, 30, 7, Side.SELL, LocalTime.of(10, 33,21)));
    
    return ordersFromDatabase;
  }
}
