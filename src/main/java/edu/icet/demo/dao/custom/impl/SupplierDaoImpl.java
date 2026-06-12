package edu.icet.demo.dao.custom.impl;

import edu.icet.demo.dao.custom.SupplierDao;
import edu.icet.demo.entity.SupplierEntity;
import edu.icet.demo.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SupplierDaoImpl implements SupplierDao {

    @Override
    public SupplierEntity search(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(SupplierEntity.class, id);
        }
    }

    @Override
    public String getLatestId() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT s.id FROM SupplierEntity s ORDER BY s.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public ObservableList<SupplierEntity> searchAll() {
        try (Session session = HibernateUtil.getSession()) {
            return FXCollections.observableArrayList(
                    session.createQuery("FROM SupplierEntity", SupplierEntity.class).list());
        }
    }

    @Override
    public boolean insert(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(supplierEntity);
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
    public boolean update(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(supplierEntity);
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
            int affected = session.createMutationQuery("DELETE FROM SupplierEntity s WHERE s.id = :id")
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
