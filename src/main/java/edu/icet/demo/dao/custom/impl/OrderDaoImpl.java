package edu.icet.demo.dao.custom.impl;

import edu.icet.demo.dao.custom.OrderDao;
import edu.icet.demo.entity.OrderDetailEntity;
import edu.icet.demo.entity.OrderEntity;
import edu.icet.demo.entity.ProductEntity;
import edu.icet.demo.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public OrderEntity search(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(OrderEntity.class, id);
        }
    }

    @Override
    public String getLatestId() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT o.id FROM OrderEntity o ORDER BY o.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public ObservableList<OrderEntity> searchAll() {
        try (Session session = HibernateUtil.getSession()) {
            return FXCollections.observableArrayList(
                    session.createQuery("FROM OrderEntity o ORDER BY o.id DESC", OrderEntity.class).list());
        }
    }

    @Override
    public boolean insert(OrderEntity orderEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(orderEntity);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean placeOrder(OrderEntity order, List<OrderDetailEntity> details) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(order);
            for (OrderDetailEntity detail : details) {
                session.persist(detail);

                ProductEntity product = session.get(ProductEntity.class, detail.getProductId());
                if (product == null || product.getQty() < detail.getQty()) {
                    throw new IllegalStateException(
                            "Not enough stock for product " + detail.getProductId());
                }
                product.setQty(product.getQty() - detail.getQty());
                session.merge(product);
            }
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(orderEntity);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.createMutationQuery("DELETE FROM OrderDetailEntity d WHERE d.orderId = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            int affected = session.createMutationQuery("DELETE FROM OrderEntity o WHERE o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
            return affected > 0;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
