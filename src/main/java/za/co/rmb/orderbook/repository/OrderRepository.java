package za.co.rmb.orderbook.repository;

import za.co.rmb.orderbook.enumerator.Side;
import za.co.rmb.orderbook.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Note: This class is to simulate a call to the database to get list of orders.
 */
public class OrderRepository {

  private long idSequence;

  public OrderRepository() {
    this.idSequence = 100L;
  }

  // Simulating database primary key generation
  public long nextSequence() {
    return idSequence++;
  }

  public Set<OrderEntity> getOrdersFromDatabase() {
    // Using a HashSet here because I do not care about order or indexes. Since the order and indexes are irrelevant at this
    // point, we can take advantage of HashSet performance basic operations (add, remove, contains and size) at constant time.
    Set<OrderEntity> ordersFromDatabase = new HashSet<>();

    // Buys orders
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 9, 40, Side.BUY, LocalDateTime.of(2023, 1, 20, 12, 0,18)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 7, 50, Side.BUY, LocalDateTime.of(2023, 1, 20, 11, 30,1)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 9, 20, Side.BUY, LocalDateTime.of(2023, 1, 20, 12, 3,22)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 8, 30, Side.BUY, LocalDateTime.of(2023, 1, 20, 13, 24,45)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 7, 50, Side.BUY, LocalDateTime.of(2023, 1, 20, 13, 24,23)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 8, 20, Side.BUY, LocalDateTime.of(2023, 1, 20, 13, 50,23)));

    // Sell orders
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 11, 50, Side.SELL, LocalDateTime.of(2023, 1, 20,15, 5,11)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 11, 40, Side.SELL, LocalDateTime.of(2023, 1, 20,9, 31,59)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 12, 20, Side.SELL, LocalDateTime.of(2023, 1, 20,10, 14,16)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 10, 5, Side.SELL, LocalDateTime.of(2023, 1, 20,10, 16,46)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 12, 10, Side.SELL, LocalDateTime.of(2023, 1, 20,10, 33,21)));
    ordersFromDatabase.add(new OrderEntity(nextSequence(), 10, 100, Side.SELL, LocalDateTime.of(2023, 1, 20,14, 33,21)));
    
    return ordersFromDatabase;
  }
}
