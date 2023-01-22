package za.co.rmb.orderbook.model;

import za.co.rmb.orderbook.entity.OrderEntity;

import java.util.Map;

/**
 * The order book as illustrated in the limit order book table.
 * @param buyOrdersMap represents the left side - listing buy orders.
 * @param sellOrdersMap represents the right side - listing sell orders.
 */
public record OrderBook(Map<Integer, Map<Long, OrderEntity>> buyOrdersMap, Map<Integer, Map<Long, OrderEntity>> sellOrdersMap) {
}
