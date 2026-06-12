package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.Supplier;
import javafx.collections.ObservableList;

public interface SupplierBo extends SuperBo {

    String generateSupplierId();

    boolean insertSupplier(Supplier supplier);

    ObservableList<Supplier> getAllSuppliers();

    ObservableList<String> getAllSupplierIds();

    boolean updateSupplier(Supplier supplier);

    boolean deleteSupplierById(String id);
}
