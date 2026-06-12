package edu.icet.demo.dao.custom;

import edu.icet.demo.dao.CrudDao;
import edu.icet.demo.entity.ProductEntity;

public interface ProductDao extends CrudDao<ProductEntity, String> {

    String getLatestId();
}
