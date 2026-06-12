package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.OrderBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.OrderDao;
import edu.icet.demo.model.Order;
import edu.icet.demo.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderBoimpl implements OrderBo {

    private final OrderDao orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generateOrderId() {
        String latestId = orderDao.getLatestId();
        if (latestId == null) {
            return "O0001";
        }
        int number = Integer.parseInt(latestId.substring(1));
        return String.format("O%04d", number + 1);
    }

    @Override
    public ObservableList<Order> getAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        orderDao.searchAll().forEach(entity ->
                orders.add(mapper.convertValue(entity, Order.class)));
        return orders;
    }
}
