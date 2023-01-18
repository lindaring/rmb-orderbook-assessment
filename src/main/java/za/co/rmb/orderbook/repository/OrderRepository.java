package za.co.rmb.orderbook.repository;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Note: This class is to simulate a call to the database to get list of orders.
 */
public class OrderRepository {

  private static long idSequence = 100L;

  // Simulating database primary key generation
  public static long nextSequence() {
    return idSequence++;
  }

  public Set<OrderEntity> getOrdersFromDatabase() {
    // Using a HashSet here because I do not care about order or indexes. Since the order and indexes are irrelevant at this
    // point, we can take advantage of HashSet performance basic operations (add, remove, contains and size) at constant time.
    Set<OrderEntity> ordersFromDatabase = new HashSet<>();

    // Buys orders
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 33, 10, Side.BUY, LocalTime.of(12, 3,18)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 33, 5, Side.BUY, LocalTime.of(11, 30,1)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 37, 7, Side.BUY, LocalTime.of(12, 0,22)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 40, 12, Side.BUY, LocalTime.of(13, 50,45)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 37, 14, Side.BUY, LocalTime.of(13, 24,23)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 37, 9, Side.BUY, LocalTime.of(13, 24,23)));

    // Sell orders
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 30, 11, Side.SELL, LocalTime.of(15, 5,11)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 30, 6, Side.SELL, LocalTime.of(9, 31,59)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 35, 9, Side.SELL, LocalTime.of(10, 14,16)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 35, 11, Side.SELL, LocalTime.of(12, 44,36)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 29, 8, Side.SELL, LocalTime.of(14, 16,46)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 30, 10, Side.SELL, LocalTime.of(10, 33,21)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 30, 7, Side.SELL, LocalTime.of(10, 33,21)));
    
    return ordersFromDatabase;
  }
}
