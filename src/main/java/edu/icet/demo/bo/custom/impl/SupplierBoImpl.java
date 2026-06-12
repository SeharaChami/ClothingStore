package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.SupplierBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.SupplierDao;
import edu.icet.demo.entity.SupplierEntity;
import edu.icet.demo.model.Supplier;
import edu.icet.demo.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierBoImpl implements SupplierBo {

    private final SupplierDao supplierDao = DaoFactory.getInstance().getDao(DaoType.SUPPLIER);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generateSupplierId() {
        String latestId = supplierDao.getLatestId();
        if (latestId == null) {
            return "S0001";
        }
        int number = Integer.parseInt(latestId.substring(1));
        return String.format("S%04d", number + 1);
    }

    @Override
    public boolean insertSupplier(Supplier supplier) {
        return supplierDao.insert(mapper.convertValue(supplier, SupplierEntity.class));
    }

    @Override
    public ObservableList<Supplier> getAllSuppliers() {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        supplierDao.searchAll().forEach(entity ->
                suppliers.add(mapper.convertValue(entity, Supplier.class)));
        return suppliers;
    }

    @Override
    public ObservableList<String> getAllSupplierIds() {
        ObservableList<String> ids = FXCollections.observableArrayList();
        supplierDao.searchAll().forEach(entity -> ids.add(entity.getId()));
        return ids;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        return supplierDao.update(mapper.convertValue(supplier, SupplierEntity.class));
    }

    @Override
    public boolean deleteSupplierById(String id) {
        return supplierDao.delete(id);
    }
}
