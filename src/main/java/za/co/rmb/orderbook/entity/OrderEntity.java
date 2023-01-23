package za.co.rmb.orderbook.entity;

import za.co.rmb.orderbook.enumerator.Side;

import java.time.LocalDateTime;

public class OrderEntity implements Comparable<OrderEntity> {

  private Long id;
  private Integer price;
  private Integer quantity;
  private Side side;
  private LocalDateTime time;

  public OrderEntity(Long id, Integer price, Integer quantity, Side side, LocalDateTime time) {
    this.id = id;
    this.price = price;
    this.quantity = quantity;
    this.side = side;
    this.time = time;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Side getSide() {
    return side;
  }

  public void setSide(Side side) {
    this.side = side;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

  /**
   * When orders are in the same price level, we need to maintain each order quantity in ascending order in terms of time.
   * First order should be in column 'Bid Order Qty 0'.
   */
  @Override
  public int compareTo(OrderEntity o) {
    if (this.getTime().isBefore(o.getTime())) {
      return  -1;
    } else if (this.getTime().isAfter(o.getTime())) {
      return 1;
    } else {
      return 0; // Not using 0 because TreeSet does not allow duplicates when values are equal.
    }
  }
}
