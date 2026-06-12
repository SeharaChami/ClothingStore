package edu.icet.demo.dao.custom.impl;

import edu.icet.demo.dao.custom.ProductDao;
import edu.icet.demo.entity.ProductEntity;
import edu.icet.demo.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductDaoImpl implements ProductDao {

    @Override
    public ProductEntity search(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(ProductEntity.class, id);
        }
    }

    @Override
    public String getLatestId() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT p.id FROM ProductEntity p ORDER BY p.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public ObservableList<ProductEntity> searchAll() {
        try (Session session = HibernateUtil.getSession()) {
            return FXCollections.observableArrayList(
                    session.createQuery("FROM ProductEntity", ProductEntity.class).list());
        }
    }

    @Override
    public boolean insert(ProductEntity productEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(productEntity);
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
    public boolean update(ProductEntity productEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(productEntity);
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
            int affected = session.createMutationQuery("DELETE FROM ProductEntity p WHERE p.id = :id")
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
