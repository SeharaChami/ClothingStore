package edu.icet.demo.dao;

import edu.icet.demo.dao.custom.impl.*;
import edu.icet.demo.util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return instance != null ? instance : (instance = new DaoFactory());
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperDao> T getDao(DaoType type) {
        switch (type) {
            case USER:
                return (T) new UserDaoImpl();
            case CUSTOMER:
                return (T) new CustomerDaoImpl();
            case PRODUCT:
                return (T) new ProductDaoImpl();
            case SUPPLIER:
                return (T) new SupplierDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();
            default:
                return null;
        }
    }
}
