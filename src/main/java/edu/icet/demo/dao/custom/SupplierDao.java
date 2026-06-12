package edu.icet.demo.dao.custom;

import edu.icet.demo.dao.CrudDao;
import edu.icet.demo.entity.SupplierEntity;

public interface SupplierDao extends CrudDao<SupplierEntity, String> {

    String getLatestId();
}
