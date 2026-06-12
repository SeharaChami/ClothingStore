package edu.icet.demo.dao.custom.impl;

import edu.icet.demo.dao.custom.CustomerDao;
import edu.icet.demo.entity.CustomerEntity;
import edu.icet.demo.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public CustomerEntity search(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(CustomerEntity.class, id);
        }
    }

    @Override
    public String getLatestId() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT c.id FROM CustomerEntity c ORDER BY c.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public ObservableList<CustomerEntity> searchAll() {
        try (Session session = HibernateUtil.getSession()) {
            return FXCollections.observableArrayList(
                    session.createQuery("FROM CustomerEntity", CustomerEntity.class).list());
        }
    }

    @Override
    public boolean insert(CustomerEntity customerEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(customerEntity);
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
    public boolean update(CustomerEntity customerEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(customerEntity);
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
            int affected = session.createMutationQuery("DELETE FROM CustomerEntity c WHERE c.id = :id")
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
