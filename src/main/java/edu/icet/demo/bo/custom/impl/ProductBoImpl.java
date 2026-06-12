package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.ProductBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.ProductDao;
import edu.icet.demo.entity.ProductEntity;
import edu.icet.demo.model.Product;
import edu.icet.demo.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductBoImpl implements ProductBo {

    private final ProductDao productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generateProductId() {
        String latestId = productDao.getLatestId();
        if (latestId == null) {
            return "P0001";
        }
        int number = Integer.parseInt(latestId.substring(1));
        return String.format("P%04d", number + 1);
    }

    @Override
    public boolean insertProduct(Product product) {
        return productDao.insert(mapper.convertValue(product, ProductEntity.class));
    }

    @Override
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        productDao.searchAll().forEach(entity ->
                products.add(mapper.convertValue(entity, Product.class)));
        return products;
    }

    @Override
    public ObservableList<String> getAllProductIds() {
        ObservableList<String> ids = FXCollections.observableArrayList();
        productDao.searchAll().forEach(entity -> ids.add(entity.getId()));
        return ids;
    }

    @Override
    public Product getProductById(String id) {
        ProductEntity entity = productDao.search(id);
        return entity == null ? null : mapper.convertValue(entity, Product.class);
    }

    @Override
    public boolean updateProduct(Product product) {
        return productDao.update(mapper.convertValue(product, ProductEntity.class));
    }

    @Override
    public boolean deleteProductById(String id) {
        return productDao.delete(id);
    }
}
