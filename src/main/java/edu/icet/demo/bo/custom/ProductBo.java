package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.Product;
import javafx.collections.ObservableList;

public interface ProductBo extends SuperBo {

    String generateProductId();

    boolean insertProduct(Product product);

    ObservableList<Product> getAllProducts();

    ObservableList<String> getAllProductIds();

    Product getProductById(String id);

    boolean updateProduct(Product product);

    boolean deleteProductById(String id);
}
