package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.Order;
import edu.icet.demo.model.OrderDetail;

import java.util.List;

public interface PlaceOrderBo extends SuperBo {

    /**
     * Persists the order with its line items and deducts the sold quantities
     * from stock - all in one transaction.
     */
    boolean placeOrder(Order order, List<OrderDetail> details);
}
