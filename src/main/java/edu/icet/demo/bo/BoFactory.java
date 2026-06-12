package edu.icet.demo.bo;

import edu.icet.demo.bo.custom.impl.*;
import edu.icet.demo.util.BoType;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory() {
    }

    public static BoFactory getInstance() {
        return instance != null ? instance : (instance = new BoFactory());
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperBo> T getBo(BoType type) {
        switch (type) {
            case USER:
                return (T) new UserBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoimpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            case SUPPLIER:
                return (T) new SupplierBoImpl();
            case ORDER:
                return (T) new OrderBoimpl();
            case PLACE_ORDER:
                return (T) new PlaceOrderBoImpl();
            default:
                return null;
        }
    }
}
