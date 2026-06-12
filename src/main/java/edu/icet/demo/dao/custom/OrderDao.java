package edu.icet.demo.dao.custom;

import edu.icet.demo.dao.CrudDao;
import edu.icet.demo.entity.OrderDetailEntity;
import edu.icet.demo.entity.OrderEntity;

import java.util.List;

public interface OrderDao extends CrudDao<OrderEntity, String> {

    String getLatestId();

    /**
     * Persists the order, its line items and the stock deduction of each
     * product in a single transaction.
     */
    boolean placeOrder(OrderEntity order, List<OrderDetailEntity> details);
}
