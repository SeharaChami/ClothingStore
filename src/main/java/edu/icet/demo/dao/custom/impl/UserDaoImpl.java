package edu.icet.demo.dao.custom.impl;

import edu.icet.demo.dao.custom.UserDao;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

    @Override
    public UserEntity search(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(UserEntity.class, id);
        }
    }

    @Override
    public UserEntity searchByUsernameOrEmail(String usernameOrEmail) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "FROM UserEntity u WHERE lower(u.name) = :v OR lower(u.email) = :v",
                            UserEntity.class)
                    .setParameter("v", usernameOrEmail.toLowerCase())
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public long count() {
        try (Session session = HibernateUtil.getSession()) {
            Long count = session.createQuery("SELECT count(u) FROM UserEntity u", Long.class)
                    .uniqueResult();
            return count == null ? 0 : count;
        }
    }

    @Override
    public String getLatestId() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT u.id FROM UserEntity u ORDER BY u.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public ObservableList<UserEntity> searchAll() {
        try (Session session = HibernateUtil.getSession()) {
            return FXCollections.observableArrayList(
                    session.createQuery("FROM UserEntity", UserEntity.class).list());
        }
    }

    @Override
    public boolean insert(UserEntity userEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(userEntity);
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
    public boolean update(UserEntity userEntity) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(userEntity);
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
            int affected = session.createMutationQuery("DELETE FROM UserEntity u WHERE u.id = :id")
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
