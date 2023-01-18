package za.co.rmb.orderbook.entity;

import za.co.rmb.orderbook.enumerator.Side;

import java.time.LocalTime;

public record OrderEntity(Long id, Integer price, Integer quantity, Side side, LocalTime time) implements Comparable<OrderEntity> {

  /**
   * When orders are in the same price level, we need to maintain each order quantity in ascending order in terms of time.
   * First order should be in column 'Bid Order Qty 0'.
   */
  @Override
  public int compareTo(OrderEntity o) {
    if (this.time().isBefore(o.time())) {
      return  -1;
    } else if (this.time().isAfter(o.time())) {
      return 1;
    } else {
      return 1; // Not using 0 because TreeSet does not allow duplicates when values are equal.
    }
  }
}
