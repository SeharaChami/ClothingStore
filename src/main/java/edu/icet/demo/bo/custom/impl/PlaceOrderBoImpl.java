package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.PlaceOrderBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.OrderDao;
import edu.icet.demo.entity.OrderDetailEntity;
import edu.icet.demo.entity.OrderEntity;
import edu.icet.demo.model.Order;
import edu.icet.demo.model.OrderDetail;
import edu.icet.demo.util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBoImpl implements PlaceOrderBo {

    private final OrderDao orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean placeOrder(Order order, List<OrderDetail> details) {
        OrderEntity orderEntity = mapper.convertValue(order, OrderEntity.class);

        List<OrderDetailEntity> detailEntities = new ArrayList<>();
        for (OrderDetail detail : details) {
            detailEntities.add(new OrderDetailEntity(
                    null,
                    order.getId(),
                    detail.getProductId(),
                    detail.getQty(),
                    detail.getUnitPrice()));
        }
        return orderDao.placeOrder(orderEntity, detailEntities);
    }
}
