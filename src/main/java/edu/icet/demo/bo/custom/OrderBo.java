package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.Order;
import javafx.collections.ObservableList;

public interface OrderBo extends SuperBo {

    String generateOrderId();

    ObservableList<Order> getAllOrders();
}
