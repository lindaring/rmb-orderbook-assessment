package za.co.rmb.orderbook.model;

import java.util.Map;
import java.util.Set;

/**
 * The order book as illustrated in the limit order book table.
 * @param buyOrdersMap represents the left side - listing buy orders.
 * @param sellOrdersMap represents the right side - listing sell orders.
 */
public record OrderBook(Map<Integer, Set<Order>> buyOrdersMap, Map<Integer, Set<Order>> sellOrdersMap) {
}
