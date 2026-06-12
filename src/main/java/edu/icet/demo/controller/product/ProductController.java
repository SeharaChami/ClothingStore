package edu.icet.demo.controller.product;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.ProductBo;
import edu.icet.demo.bo.custom.SupplierBo;
import edu.icet.demo.model.Category;
import edu.icet.demo.model.Product;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Product catalogue management (add / update / delete / list).
 */
public class ProductController implements Initializable {

    @FXML
    private TableView<Product> tblProducts;

    @FXML
    private TableColumn<Product, String> colProductId;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, String> colCategory;

    @FXML
    private TableColumn<Product, Integer> colSize;

    @FXML
    private TableColumn<Product, Integer> colQty;

    @FXML
    private TableColumn<Product, Double> colUnitPrice;

    @FXML
    private TableColumn<Product, String> colSupId;

    @FXML
    private Label lblProductId;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtSize;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private ComboBox<String> categoryCmb;

    @FXML
    private ComboBox<String> cmbSupId;

    @FXML
    private Button btnNewProduct;

    @FXML
    private Button btnUpdateProduct;

    @FXML
    private Button btnDeleteProduct;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnBack;

    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);

    private String selectedId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));

        categoryCmb.setItems(FXCollections.observableArrayList(
                Category.GENTS.name(), Category.LADIES.name(), Category.KIDS.name()));

        tblProducts.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                selectedId = selected.getId();
                lblProductId.setText(selected.getId());
                txtProductName.setText(selected.getName());
                txtSize.setText(String.valueOf(selected.getSize()));
                txtQty.setText(String.valueOf(selected.getQty()));
                txtUnitPrice.setText(String.valueOf(selected.getPrice()));
                categoryCmb.setValue(selected.getCategory());
                cmbSupId.setValue(selected.getSupId());
            }
        });

        refresh();
    }

    @FXML
    void addProductOnAction(ActionEvent event) {
        Product product = readForm(null);
        if (product == null) {
            return;
        }
        try {
            product.setId(productBo.generateProductId());
            if (productBo.insertProduct(product)) {
                Alerts.info("Product added", "Product \"" + product.getName() + "\" was added to the catalogue.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void updateProductOnAction(ActionEvent event) {
        if (selectedId == null) {
            Alerts.info("No product selected", "Select a product in the table to update it.");
            return;
        }
        Product product = readForm(selectedId);
        if (product == null) {
            return;
        }
        try {
            if (productBo.updateProduct(product)) {
                Alerts.info("Product updated", "Product " + selectedId + " was updated successfully.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void deleteProductOnAction(ActionEvent event) {
        if (selectedId == null) {
            Alerts.info("No product selected", "Select a product in the table to delete it.");
            return;
        }
        if (!Alerts.confirm("Delete product", "Delete product " + selectedId + " permanently?")) {
            return;
        }
        try {
            if (productBo.deleteProductById(selectedId)) {
                Alerts.info("Product deleted", "Product " + selectedId + " was removed from the catalogue.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void clearOnAction(ActionEvent event) {
        clearForm();
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Navigation.navigate(btnBack, UserSession.isAdmin()
                ? "/view/user/admin-form.fxml"
                : "/view/user/employee-home.fxml");
    }

    private Product readForm(String id) {
        String name = txtProductName.getText().trim();
        String category = categoryCmb.getValue();
        String supId = cmbSupId.getValue();

        if (name.isEmpty() || category == null) {
            Alerts.error("Missing information", "Please fill in at least the product name and category.");
            return null;
        }

        int size;
        int qty;
        double price;
        try {
            size = txtSize.getText().isBlank() ? 0 : Integer.parseInt(txtSize.getText().trim());
            qty = Integer.parseInt(txtQty.getText().trim());
            price = Double.parseDouble(txtUnitPrice.getText().trim());
        } catch (NumberFormatException e) {
            Alerts.error("Invalid numbers", "Size and quantity must be whole numbers and the price a valid amount.");
            return null;
        }
        if (qty < 0 || price < 0) {
            Alerts.error("Invalid values", "Quantity and price cannot be negative.");
            return null;
        }
        return new Product(id, name, size, qty, category, price, supId);
    }

    private void refresh() {
        try {
            tblProducts.setItems(productBo.getAllProducts());
            cmbSupId.setItems(supplierBo.getAllSupplierIds());
            lblProductId.setText(productBo.generateProductId());
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    private void clearForm() {
        selectedId = null;
        tblProducts.getSelectionModel().clearSelection();
        txtProductName.clear();
        txtSize.clear();
        txtQty.clear();
        txtUnitPrice.clear();
        categoryCmb.setValue(null);
        cmbSupId.setValue(null);
        try {
            lblProductId.setText(productBo.generateProductId());
        } catch (Exception ignored) {
            lblProductId.setText("(auto)");
        }
    }
}
